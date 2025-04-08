## Preamble

`YuvImage.compressToJpeg()` produces JPEGs with ringing (or similar) artifacts when the image width is not divisible
by 16.
The underlying library, `libjpeg-turbo`, is expected to handle such cases by padding the image with black pixels.
You can find evidence of this behavior in
the [source code](https://github.com/libjpeg-turbo/libjpeg-turbo/blob/2a0c86278249e7a3c3429caff24c06a50048d772/src/jccoefct.c#L182)
of the function that is called internally by `YuvImage.compressToJpeg()`.

Additionally, I recompiled `libjpeg-turbo` with logging enabled, replaced the library on an AOSP device, and confirmed
that the padding logic is indeed executed when the image width is not divisible by 16.
Nevertheless, the final JPEG still shows ringing (or similar) artifacts.

I also added a test case where the dummy black pixels are included as part of the original image itself. In that case,
no artifacts are present.
This serves as further proof that the issue lies in how the internal algorithm handles image widths that are not
divisible by 16.

## How to Test

Run the `AndroidTest` to reproduce the issue. The resulting JPEGs can be found on the device at the following path:
`Pictures/TestDebug`.
