package edu.rit.maocad.tipcalculator

import android.graphics.Paint.Align
import android.inputmethodservice.Keyboard
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.rit.maocad.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Calculator()
                }
            }
        }
    }
}

@Composable
fun Calculator() {

    var billTotalState by remember {
        mutableStateOf(0.0)
    }
    var customTipState by remember {
        mutableStateOf(.18f)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BillRow(
            billTotal = billTotalState,
            updateTotal = { newTotal ->
                billTotalState = newTotal
            }
        )

        HeadingRow()
        
        TipRow(billTotal = billTotalState)
        
        TotalRow(billTotal = billTotalState)

        CustomRow(customTip = customTipState, updateCustom = {
            customTipState = it
        })
        
        CustomTotalRow(billTotal = billTotalState, customTip = customTipState)
    }
}

@Composable
fun Label(
    labelText: String,
    align: TextAlign
) {
    Text(
        text = labelText,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = align,
        color = Color.Black,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    )
} // Label

@Composable
fun TipAndTotalValueField(
    value: Double,
    align: TextAlign
) {
    BasicTextField(
        value = String.format("%.02f", value),
        onValueChange = {},
        enabled = false,
        textStyle = TextStyle(
            textAlign = align,
            color = Color.Black,
            fontSize = 18.sp
        )
    )
} // TipAndValueField

@Composable
fun BillRow(
    billTotal: Double,
    updateTotal: (Double) -> Unit
){
    var badInput by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Label(labelText = "Bill Total:", align = TextAlign.Center)

//        // EXAMPLE 1: BasicTextField
//        BasicTextField(
//            value = billTotal.toString(),
//            onValueChange = {
//                val parsed = it.toDoubleOrNull() ?: 0.0
//                if(parsed < 999999.99) {
//                    updateTotal(parsed)
//                }
//             },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number,
//                imeAction = ImeAction.Done
//            ),
//            modifier = Modifier.fillMaxWidth()
//        ) // BasicTextField

        // EXAMPLE 2: TextField & OutlinedTextField
        OutlinedTextField(
            value = billTotal.toString(),
            label = {
                Text(text = "Bill Total")
            },
            onValueChange = {
                val parsed = it.toDoubleOrNull() ?: 0.0
                if(parsed < 999999.99) {
                    badInput = false
                    updateTotal(parsed)
                } else {
                    badInput = true
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Green,
                unfocusedBorderColor = Color.Blue,
                focusedLabelColor = MaterialTheme.colors.secondary,
                cursorColor = MaterialTheme.colors.primaryVariant
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = badInput,
            modifier = Modifier.fillMaxWidth()
        ) // TextField
    }
}

@Composable
fun HeadingRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Label(labelText = "", align = TextAlign.End)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Label(labelText = "10%", align = TextAlign.Center)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Label(labelText = "15%", align = TextAlign.Center)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Label(labelText = "20%", align = TextAlign.Center)
        }
    }
} // HeadingRow

@Composable
fun TipRow(billTotal: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Label(labelText = "Tip:", align = TextAlign.End)
        }

        val tips = arrayOf(0.10, 0.15, 0.20)

        for(tip in tips) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TipAndTotalValueField(
                    value = billTotal * tip,
                    align = TextAlign.Center
                )
            }
        }
    } // Row
} // TipRow

@Composable
fun TotalRow(billTotal: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Label(labelText = "Total:", align = TextAlign.End)
        }
        
        val tips = arrayOf(0.10, 0.15, 0.20)
        
        for(tip in tips) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TipAndTotalValueField(
                    value = billTotal * tip + billTotal, 
                    align = TextAlign.Center
                )
            }
        }
    } // Row
} // TotalRow

@Composable
fun CustomRow(
    customTip: Float,
    updateCustom: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Label(labelText = "Custom:", align = TextAlign.End)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider(
                value = customTip,
                onValueChange = { customTip ->
                    updateCustom(customTip)
                },
                valueRange = .0f .. 1.0f,
                onValueChangeFinished = {
                    // Do something..
                },
                steps = 0,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.secondary,
                    activeTrackColor = MaterialTheme.colors.secondary
                )
            ) // Slider
        } // Column

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Label(
                labelText = "${(customTip * 100f).toInt()}%",
                align = TextAlign.End
            )
        }
    }
}

@Composable
fun CustomTotalRow(billTotal: Double, customTip: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        )
        {
            Label(labelText = "Tip:", align = TextAlign.End)
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            TipAndTotalValueField(value = billTotal * customTip, align = TextAlign.Center)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        )
        {
            Label(labelText = "Total:", align = TextAlign.End)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            TipAndTotalValueField(value = billTotal * customTip + billTotal, align = TextAlign.Center)
        }
    } // Row
} // CustomTotalRow

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        Calculator()
    }
}

















