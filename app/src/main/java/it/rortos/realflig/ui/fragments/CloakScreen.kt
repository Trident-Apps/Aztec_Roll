package it.rortos.realflig.ui.fragments

import android.os.Bundle
import it.rortos.realflig.R
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun CloakScreen(navController: NavController) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.cloak_screen),
                contentDescription = "cloak_screen",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        fontStyle = FontStyle.Italic,
                        text = "Find where is Eagle",
                        fontSize = 32.sp,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic3), contentDescription = null,
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                    DefaultImage(navController)
                }
            }
        }
    }
}

@Composable
fun DefaultImage(navController: NavController) {
    AndroidView(factory = { context ->
        ImageView(context).apply {
            val imageResource = ContextCompat.getDrawable(context, R.drawable.ic4)
            setImageDrawable(imageResource)
            setOnClickListener {
                play(it as ImageView, navController)
            }
        }
    })
}

private fun play(view: ImageView, navController: NavController) {

    view.animate().apply {
        duration = 1000L
        rotationYBy(360f)
    }.withEndAction {
        val list = listOf(1, 2, 3).shuffled()
        if (list[0] == 1) {
            view.setImageResource(R.drawable.ic3)
            view.animate().apply {
                duration = 1000L
                rotationYBy(360f)
            }.withEndAction {
                navigateToResult(navController, "You Won")
            }
        } else {
            val ivList = listOf(R.drawable.ic, R.drawable.ic2).shuffled()
            view.setImageResource(ivList[0])
            view.isClickable = false
        }
    }
}

private fun navigateToResult(navController: NavController, string: String) {
    with(Bundle()) {
        this.putString("resText", string)
        navController.navigate(R.id.resultFragment, this)
    }
}


//@Preview
//@Composable
//fun Preview() {
//    CloakScreen()
//}