package com.streamliners.ktscript

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.streamliners.base.BaseActivity
import com.streamliners.base.ext.showMessageDialog
import com.streamliners.base.uiEvent.UiEventDialogs
import com.streamliners.compose.comp.textInput.TextInputLayout
import com.streamliners.compose.comp.textInput.state.TextInputState
import com.streamliners.compose.comp.textInput.state.nullableValue
import com.streamliners.ktscript.ui.theme.MyApplicationTheme
import com.streamliners.utils.safeLet
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import javax.script.ScriptEngineManager
import javax.script.ScriptException

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
class MainActivity : BaseActivity() {

    override var buildType: String = BuildConfig.BUILD_TYPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScriptingTestScreen()
                    UiEventDialogs()
                }
            }
        }
    }

    @Composable
    private fun ScriptingTestScreen() {
        val expression = remember {
            mutableStateOf(
                TextInputState("Expression", value = "a + b")
            )
        }

        val a = remember {
            mutableStateOf(
                TextInputState("a", value = "2")
            )
        }

        val b = remember {
            mutableStateOf(
                TextInputState("b", value = "5")
            )
        }

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            TextInputLayout(state = expression)
            TextInputLayout(state = a)
            TextInputLayout(state = b)

            Button(
                onClick = {
                    safeLet(
                        expression.nullableValue(),
                        a.nullableValue(),
                        b.nullableValue()
                    ) { e, x, y ->
                        val result = executeScript(e, x, y)
                        showMessageDialog("Result", result)
                    }
                }
            ) {
                Text(text = "Evaluate")
            }
        }
    }

    fun executeScript(
        expression: String,
        a: String,
        b: String
    ): String {
        val kotlinCode = """
        fun multiply(a: Int, b: Int): Int { return $expression }
        
        print(multiply($a, $b))
    """.trimIndent()

        return try {
            // Create a ByteArrayOutputStream to capture the script's output
            val outputStream = ByteArrayOutputStream()
            val printStream = PrintStream(outputStream)

            // Save the original standard output stream
            val originalOut = System.out

            // Redirect the standard output stream to the ByteArrayOutputStream
            System.setOut(printStream)

            // Create the script engine
            val engineManager = ScriptEngineManager()
            val engine = engineManager.getEngineByExtension("kts")

            // Execute the script
            engine.eval(kotlinCode)

            // Restore the original standard output stream
            System.setOut(originalOut)

            outputStream.toString()
        } catch (e: ScriptException) {
            e.printStackTrace()
            "ERROR"
        }
    }
}
