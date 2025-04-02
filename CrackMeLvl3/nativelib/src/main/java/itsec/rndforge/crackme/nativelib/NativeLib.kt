package itsec.rndforge.crackme.nativelib

class NativeLib {

    external fun secretKey(): String

    external fun verify(input: String, secretKey: String): Boolean

    companion object {
        init {
            System.loadLibrary("nativelib")
        }
    }
}