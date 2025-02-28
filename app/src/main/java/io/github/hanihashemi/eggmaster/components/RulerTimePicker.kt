package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme

@Composable
fun RulerTimePicker(
    modifier: Modifier = Modifier,
    value: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit,
) {
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = value)
    var selectedValue by remember { mutableIntStateOf(value) }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemScrollOffset }
            .collect {
                selectedValue = scrollState.firstVisibleItemIndex
                onValueChange(selectedValue)
            }
    }

    BoxWithConstraints {
        constraints

    }

    BoxWithConstraints(
        modifier = modifier
            .padding(
                vertical = Dimens
                    .PaddingXXLarge
            )
            .height(150.dp)
            .fillMaxWidth()
    ) {
        val boxWithConstraintsScope = this

        LazyRow(
            state = scrollState,
            contentPadding = PaddingValues(horizontal = boxWithConstraintsScope.maxWidth / 2 - 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            items(maxValue + 1) { value ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .width(if (value % 5 == 0) 3.dp else 1.dp)
                            .height(if (value % 5 == 0) 20.dp else 10.dp)
                            .background(Color.LightGray)
                    )
                    if (value % 5 == 0) {
                        Text(
                            text = value.toString(),
                            color = Color.LightGray,
                            fontSize = 16.sp,
                            modifier = Modifier.offset(y = 4.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(3.dp)
                .height(30.dp)
                .background(Color.White)
        )

        Text(
            text = selectedValue.toString(),
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 105.dp)
        )
    }
}

@Preview(device = "id:medium_phone")
@Composable
private fun RulerTimePickerPreview() {
    EggMasterTheme {
        RulerTimePicker(
            maxValue = 60,
            value = 10,
            onValueChange = {},
        )
    }
}