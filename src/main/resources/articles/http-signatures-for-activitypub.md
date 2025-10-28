## Introduction

If you don't know me, you wouldn't know about Aster. I've been writing my own fediverse server since 2024 and it's been rough.
It's gone through two TypeScript rewrites, and then another Kotlin one. As of writing I just finished HTTP signatures, 
which is why I'm writing this. Originally, I used a library and didn't think much of them or understand how they worked.
Now I do (for the most part), and I'm documenting it in the hope that somebody finds it useful.

## Why?

In ActivityPub, HTTP signatures are used to verify that an activity is valid and that the actor that's supposedly sent it
actually has. It prevents a lot of bad things from happening, like:

- Other instance reports to be another one.
- Domain expires, is bought by someone else, and they impersonate the actor's registered as being on that domain.

All of these examples aren't possible<sup>[1]</sup> because the impersonator doesn't have the actor's private key, so they can't sign 
an activity.

## Structure

When a request is signed, the signature has nothing to do with the body of the request. Signatures are often required for 
GET and POST requests, so instead, digests are used for bodies. What ends up being signed (the "signing string") looks like this:

```
(request-target): post /inbox
date: Sun, 26 Oct 2025 18:38:43 GMT
host: aster.remlit.site
digest: SHA-256=Uode+sf+HWwoHvCOKKqYaVBi0yP1xaO9cKfaxYekHmE=
```

...and the POST request's Signature header should<sup>[2]</sup> look like:

```
keyId="https://remlit.site/users/a2mjqz9uyru1n7y4#main-key",
headers="(request-target) date host digest",
algorithm="hs2019",
signature="PWSP9FnEvlKuZXmfu0E8JxRfhF6vAPj0ytgO0VPCGgTQBcpMP+qIXojfBFq5c8IKE0gNUPdUcZAjQSFQ0MdcJVQdtRJryySewX6NAStmDLgRMIbk38oxn7kvziMWDh9x/zDlwEQ/TO4jzwNpzdvJoiqrqjdoRVWryV9qDDbpGmomWaQx48YLlg/XY+zBqlOmoanNr9CYcueaPd4FWTmiY+9Fvw2EPsSY16z9yYDQR2Fb6XreUx4STRdUnM8tAqbZgeT5OVE1OH7GpFmJXm5wxzroDa6AuGceSSSyT71ioKiETiNFbPtBoowIDEgnznFdqTH/lgagsxomb63LNMk77YI7SAAGTdBQZ/FiigXIt88C4JXxpRNxCg87sY/93yKgTGcO1hzDgohB+gn93Dc+y2FSSpOlcs0Xm/q2QdaNIPe+xl3SbO9FnDcnd4S+tUMrKu8vm9Aie18MS9DHzlrLUGAvFsYT+4E4NyTBUFobkXHqa1vnzTodPiKPbLrYdZbNN2CKASf1z7S8WtrP11VIlXB1Gp/GRhST6NiThEpnl67T/TG97zoh6QeprM3zkicxNxX+ekZJnqdAz0E4FrPwgZjJLL5Eh4JWan1u8KBT42I+lDXKJ7Xet3EU3E9i/x0lMvm2K3f8E7mW3QkYvGjRM0/t8TDDHSkKQTzrfuVAL/s="
```

The signature on the Signature header is the base64 encoded value of the signature of the signing string. The private key
used to generate the signature is from the key of the actor listed on keyId.

On the request, there must be the specified headers (except (request-target)). If this were a GET request, the digest could be omitted.

## Validating

To validate this signature, you first have to recreate the signing string. When I was first trying to implement signatures 
into the Kotlin rewrite of Aster, I thought it was the request body the signature validated. HTTP signatures have nothing
to do with the request's body.

Once you recreate the signing string, you need to get the public key referenced in keyId. Then you can use whatever signature
library that supports SHA256withRSA. In Java, the standard `java.security.Signature` works.

If a body is present, make sure you validate its digest. In Java, `java.security.MessageDigest` also works.

## Common Adaptations

A lot of implementers of ActivityPub have to adapt to make the standard work, and HTTP signatures are one of those things.
Often, an "instance actor" is needed for authorized fetch<sup>[3]</sup> to work where there's no specific actor requesting something.

## Additional Notes

If you'd like to see examples of how all this works, the most correct implementation to look at is [Iceshrimp.NET](https://iceshrimp.net).
I usually test Aster against it for everything (and would recommend you do, too), since they care way more about the 
specifics and following the rules than I do. If you'd like to see how I've handled signatures, check out Aster's [ApValidationService](https://github.com/ihateblueb/aster-kt/blob/main/src/main/kotlin/site/remlit/aster/service/ap/ApValidationService.kt).

## Footnotes

**1:** In ideal conditions. Signature validation is probably the most common vulnerability in fediverse software. See: https://github.com/misskey-dev/misskey/security/advisories/GHSA-7vgr-p3vc-p4h2, https://github.com/mastodon/mastodon/security/advisories/GHSA-3fjr-858r-92rw.

**2:** Not actually. This has added new lines for readability.

**3:** Authorized fetch is when a GET endpoint requires HTTP signatures as authentication to make sure only certain actors (followers for example) can access a resource. It's also used to further enforce instance blocks.