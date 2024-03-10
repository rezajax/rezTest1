package am.reza.test1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import am.reza.test1.ui.theme.RezTestTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RezTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var urlText by remember { mutableStateOf("") }
                        var responseText by remember { mutableStateOf("") }

                        // Textbox for entering URL
                        TextField(
                            value = urlText,
                            onValueChange = { urlText = it },
                            label = { Text("Enter URL") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to execute request
                        Button(onClick = {
                            GlobalScope.launch(Dispatchers.IO) {
                                responseText = executeRequest(urlText)
                            }
                        }) {
                            Text("Execute Request")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Text to display response
                        Text(text = responseText)
                    }
                }
            }
        }
    }

    // Function to execute HTTP request
    private fun executeRequest(urlString: String): String {
        return try {
            var urlStr = urlString
            if (!urlStr.startsWith("http://") && !urlStr.startsWith("https://")) {
                // If URL doesn't start with http:// or https://, prepend https://
                urlStr = "https://$urlStr"
            }
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val output = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append('\n')
            }
            reader.close()
            output.toString()
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RezTestTheme {
        // Preview code
    }
}
