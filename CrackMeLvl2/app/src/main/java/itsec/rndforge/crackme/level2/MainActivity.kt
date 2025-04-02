package itsec.rndforge.crackme.level2

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
import itsec.rndforge.crackme.level2.ui.theme.CrackMeLvl2Theme
import itsec.rndforge.crackme.nativelib.NativeLib

class MainActivity : ComponentActivity() {

    private lateinit var securePreferences: SecurePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        securePreferences = SecurePreferences.create(this)

        val key = NativeLib()
        securePreferences.saveString(SecurePreferences.KEY, key.stringFromJNI())

        setContent {
            CrackMeLvl2Theme {
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
        return securePreferences.getString(SecurePreferences.KEY) == input
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

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    CrackMeLvl2Theme {
        Greeting(
            object : Interface {
                override fun onVerifyClick(input: String): Boolean {
                    return true
                }
            }
        )
    }
}