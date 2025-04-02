package itsec.rndforge.crackme.level3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import itsec.rndforge.crackme.level3.ui.theme.CrackMeLvl3Theme
import itsec.rndforge.crackme.nativelib.NativeLib

class MainActivity : ComponentActivity() {

    private lateinit var nativeLib: NativeLib
    private lateinit var secureFileHandler: SecureFileHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        nativeLib = NativeLib()

        secureFileHandler = SecureFileHandler.create(this)
        secureFileHandler.save(nativeLib.secretKey())

        setContent {
            CrackMeLvl3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Greeting(
                            object : Interface {
                                override fun onVerifyClick(input: String): Boolean {
                                    return verify(input)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun verify(input: String): Boolean {
        val secretKey = secureFileHandler.read()
        return nativeLib.verify(input, secretKey)
    }
}

interface Interface {
    fun onVerifyClick(input: String): Boolean
}

@Composable
fun Greeting(
    verify: Interface
) {
    Column(modifier = Modifier.padding(16.dp)) {

        var secretKey by remember { mutableStateOf("") }
        var resultMsg by remember { mutableStateOf("") }

        TextField(value = secretKey, onValueChange = {
            secretKey = it
        },
            label = {
                Text(text = "What is the secret key?")
            })
        Button(onClick = {
            resultMsg = if (verify.onVerifyClick(secretKey)) {
                "Yay! You made it :D"
            } else {
                ":( Try again"
            }
        }) {
            Text(text = "Verify")
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))
        Text(text = resultMsg, style = TextStyle(fontSize = 20.sp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CrackMeLvl3Theme {
        Greeting(
            object : Interface {
                override fun onVerifyClick(input: String): Boolean {
                    return true
                }
            }
        )
    }
}