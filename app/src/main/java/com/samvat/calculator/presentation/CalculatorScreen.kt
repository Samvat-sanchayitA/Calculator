package com.samvat.calculator.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samvat.calculator.model.CalculatorAction
import com.samvat.calculator.model.CalculatorButton
import com.samvat.calculator.model.CalculatorOperation

@Composable
fun ComposeButton(
    modifier: Modifier = Modifier, symbol: String, color: Color = Color.White,
    textStyle: TextStyle = TextStyle(),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(color)
            .clickable {
                onClick()
            }
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = symbol,
            style = textStyle,
            fontSize = 36.sp,
            color = Color.White
        )
    }
}

@Composable
fun CalculatorScreen() {
    val viewModel = viewModel<CalculatorViewModel>()
    val state by viewModel.state.collectAsState()
    Log.i("Screen", state.input1)
    val buttonSpacing = 8.dp
    val row1 = listOf(
        CalculatorButton("AC", CalculatorAction.Clear),
        CalculatorButton("^", CalculatorAction.Operation(CalculatorOperation.Power)),
        CalculatorButton("%", CalculatorAction.Operation(CalculatorOperation.Modulo)),
        CalculatorButton("/", CalculatorAction.Operation(CalculatorOperation.Divide))
    )

    val row2 = listOf(
        CalculatorButton("7", CalculatorAction.Number(7)),
        CalculatorButton("8", CalculatorAction.Number(8)),
        CalculatorButton("9", CalculatorAction.Number(9)),
        CalculatorButton("x", CalculatorAction.Operation(CalculatorOperation.Multiply))
    )

    val row3 = listOf(
        CalculatorButton("4", CalculatorAction.Number(4)),
        CalculatorButton("5", CalculatorAction.Number(5)),
        CalculatorButton("6", CalculatorAction.Number(6)),
        CalculatorButton("-", CalculatorAction.Operation(CalculatorOperation.Subtract))
    )

    val row4 = listOf(
        CalculatorButton("1", CalculatorAction.Number(1)),
        CalculatorButton("2", CalculatorAction.Number(2)),
        CalculatorButton("3", CalculatorAction.Number(3)),
        CalculatorButton("+", CalculatorAction.Operation(CalculatorOperation.Add))
    )
    val row5 = listOf(
        CalculatorButton("0", CalculatorAction.Number(0)),
        CalculatorButton(".", CalculatorAction.Decimal),
        CalculatorButton("DEL", CalculatorAction.Delete),
        CalculatorButton("=", CalculatorAction.Calculate)
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
       // horizontalAlignment = Alignment.Horizontal(),
        verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {

        Text(
            text = state.input1 + (state.operation?.symbol ?: "") +  state.input2,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth().weight(1f)
                .padding(top = 32.dp),
            fontWeight = FontWeight.Light,
            fontSize = 60.sp,
            color = Color.Black,
            maxLines = 2
        )

        NextRow(buttonSpacing, row1, onEvent = viewModel::onAction)
        NextRow(buttonSpacing, row2, onEvent = viewModel::onAction)
        NextRow(buttonSpacing, row3, onEvent = viewModel::onAction)
        NextRow(buttonSpacing, row4, onEvent = viewModel::onAction)
        NextRow(buttonSpacing, row5, onEvent = viewModel::onAction)
    }
}

@Composable
private fun NextRow(
    buttonSpacing: Dp,
    row: List<CalculatorButton>,
    onEvent: (CalculatorAction) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (j in 0..3) {
            ComposeButton(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(buttonSpacing),
                color = if(row[j].symbol == "=" || row[j].symbol == "AC") Color.Magenta else Color.Gray,
                symbol = row[j].symbol,
                onClick = { onEvent(row[j].action) })
        }
    }
}


