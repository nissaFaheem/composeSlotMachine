package com.example.composothon

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.composothon.ui.theme.ComposothonTheme
import com.example.composothon.ui.theme.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : ComponentActivity() {
    private var squadDataList = ArrayList<SquadObject>()

    // private var squadMemberList = ArrayList<String>()
    private var squadNameList = ArrayList<String>()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setAllData()
        setContent {
            ComposothonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //  color = colorResource(id = R.color.lightGrey)
                ) {

                    // AnimVisibility()
                    loadSlotMachine()

                }
            }
        }

    }

    private fun setAllData() {
        squadDataList =
            Gson().fromJson(Constants.squadJson, object : TypeToken<List<SquadObject?>?>() {}.type)
        // Log.e("pritingvalue", Constants.squadJson)

        for (squad in squadDataList!!) {
            //  squadMemberList?.add(squad.name!!)
            squadNameList?.add(squad.squad!!)

            /* if(!squadNameList?.contains(squad.name)) {
                 squadNameList?.add(squad.squad!!)
             }*/
        }
        Collections.shuffle(squadNameList)

    }


    @Composable
    fun setImageAndText(
        item: String,
        mod: Modifier,
        resourse: Int,
        ht: Int = 40,
        sh: Shape = CircleShape, bor: Int = 0, ts: Int = 10
    ) {
        var resource2: Int? = 0
        if (resourse == null || resourse == 0) {
            resource2 = R.drawable.defaultman
        } else {
            resource2 = resourse

        }
        GlideImage(

            modifier = Modifier
                .height(ht.dp)
                .clip(sh)
                .aspectRatio(1f),
            imageModel = resource2,
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.Fit,

            // shows a placeholder while loading the image.
            placeHolder = ImageBitmap.imageResource(R.drawable.defaultman)
            // shows an error ImageBitmap when the request failed.
        )

        Text(
            text = item,
            modifier = mod,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h1, fontSize = ts.sp
        )

    }


    @OptIn(
        ExperimentalAnimationApi::class,
        androidx.compose.material.ExperimentalMaterialApi::class
    )
    @Composable
    fun loadSlotMachine() {
        var gameOver by remember { mutableStateOf(false) }

        Image(
            painter = painterResource(id = R.drawable.bg2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            modifier = Modifier
                .border(1.dp, Black, RectangleShape)
                .fillMaxSize()
        ) {
          //states
            var questionState by remember { mutableStateOf(false) }
            var rightAnswer by remember { mutableStateOf(false) }

            var count by remember { mutableStateOf(0) }
            var squadCount by remember { mutableStateOf(0) }
            var expanded by remember { mutableStateOf(false) }
            var scoreCount by remember { mutableStateOf(0) }

            val timer = object : CountDownTimer(1000, 140) {
                override fun onTick(millisUntilFinished: Long) {
                    // count++
                    count = getRandomNumber(0, 25)
                }

                override fun onFinish() {}
            }

            val squadTimer = object : CountDownTimer(1200, 120) {
                override fun onTick(millisUntilFinished: Long) {
                    //   squadCount++
                    squadCount = getRandomNumber(0, 25)
                }

                override fun onFinish() {

                    expanded = true
                    questionState = true
                    Log.e(
                        "values compared:",
                        squadDataList?.get(count)?.name + squadDataList?.get(count)?.squad + "..." + squadNameList.get(
                            squadCount
                        )
                    )
                    if (squadDataList?.get(count + 1)?.squad.equals(
                            squadNameList.get(squadCount + 1),
                            ignoreCase = true
                        )
                    ) {
                        scoreCount++

                        rightAnswer = true
                    } else {
                        rightAnswer = false
                        scoreCount--

                    }
                    if (scoreCount <= -5) {
                        gameOver = true
                    }
//                    var selectedCombo =
//                        SquadObject(squadDataList.get(count).name, squadNameList.get(squadCount))
//                    expanded = true
//                    questionState = true
//                    if (squadDataList?.contains(selectedCombo)!!) {
//                        rightAnswer = true
//                        Toast.makeText(
//                            applicationContext, "u won",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } else {
//                        rightAnswer = false
//                        Toast.makeText(
//                            applicationContext, " u lost",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
                }
            }



            //start building UI
            setBoldText()
            setSubtitle()
            animateScore(scoreCount)

            //show animation
            if (expanded) {
                Expanded(rightAnswer)
            } else {
                ContentIcon()
            }


            Spacer(modifier = Modifier.height(40.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .clip(RoundedCornerShape(10))
                    .padding(horizontal = 20.dp)
                    .border(
                        border = BorderStroke(
                            width = 3.dp,
                            Color.Yellow
                        ), shape = RoundedCornerShape(4)
                    )
                    .background(
                        colorResource(id = R.color.white)
                    )
                    .padding(10.dp)
            ) {
                var list = squadDataList

                ////

                animateMemberNamesUi(count, list)


                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(50.dp)
                        .weight(0.5f)
                )
                {
                    androidx.compose.animation.AnimatedVisibility(visible = questionState) {

                        if (rightAnswer) {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(
                                    R.raw.thatsryt
                                )
                            )
                            LottieAnimation(
                                composition, modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                            )

                        } else {
                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(
                                    R.raw.wrong
                                )
                            )
                            LottieAnimation(
                                composition, modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(
                            colorResource(id = R.color.white)
                        )
                )
                {
                    animateSquadNameList(squadCount)


                }


            }

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    questionState = false
                    expanded = false
                    timer.start()
                    squadTimer.start()
                },
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
            ) {
                Text(
                    text = "Click To Try Your Luck!", style = MaterialTheme.typography.h1, fontSize = 20.sp
                )

            }
        }
       if(gameOver) {
           gameOverAnimation()
        }

    }

    @Composable
    private fun gameOverAnimation() {
        Surface(
            modifier = Modifier
                .background(
                    colorResource(
                        id = R.color.black_transparent
                    )
                )
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    R.raw.gameover
                )
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LottieAnimation(
                    composition, modifier = Modifier
                        .height(300.dp)
                        .width(300.dp), alignment = Center
                )


            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun animateSquadNameList(squadCount: Int) {
        var squadList = squadNameList
        AnimatedContent(
            targetState = squadCount,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInVertically() + fadeIn() with
                            slideOutVertically() + fadeOut()
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInVertically() + fadeIn() with
                            slideOutVertically() + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }
        ) { squadC ->
            LazyColumn(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                items(items = squadList, itemContent = { item ->

                    Log.e("values shown 2:", squadList.get(squadC))

                    setImageAndText(
                        "                   ",
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        getDrawableFromString(drawableName = squadList.get(squadC) + ".jpg"),
                        sh = RectangleShape, bor = 0
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    setImageAndText(
                        "                   ",
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        getDrawableFromString(drawableName = squadList.get(squadC + 1) + ".jpg"),
                        100,
                        sh = RectangleShape, bor = 3, ts = 18
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    setImageAndText(
                        "                   ",
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        getDrawableFromString(drawableName = squadList.get(squadC + 2) + ".jpg"),
                        sh = RectangleShape, bor = 0
                    )
                })

            }


        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainActivity.animateMemberNamesUi(
        count: Int,
        list: ArrayList<SquadObject>
    ) {
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(
                    colorResource(id = R.color.white)
                )
        )
        {

            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    // Compare the incoming number with the previous number.
                    if (targetState > initialState) {
                        // If the target number is larger, it slides up and fades in
                        // while the initial (smaller) number slides up and fades out.
                        slideInVertically() + fadeIn() with
                                slideOutVertically() + fadeOut()
                    } else {
                        // If the target number is smaller, it slides down and fades in
                        // while the initial number slides down and fades out.
                        slideInVertically() + fadeIn() with
                                slideOutVertically() + fadeOut()
                    }.using(
                        // Disable clipping since the faded slide-in/out should
                        // be displayed out of bounds.
                        SizeTransform(clip = false)
                    )
                }
            ) { targetCount ->
                LazyColumn(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    items(items = list, itemContent = { item ->
                        setImageAndText(
                            list.get(targetCount).name,
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            getDrawableFromString(list.get(targetCount).photo), bor = 0
                        )
                        Spacer(modifier = Modifier.height(20.dp))


                        setImageAndText(
                            list.get(targetCount + 1).name,
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            getDrawableFromString(list.get(targetCount + 1).photo),
                            100, bor = 3, ts = 18
                        )
                        Spacer(modifier = Modifier.height(20.dp))


                        Log.e(
                            "values shown 1:",
                            list.get(targetCount + 2).name + list.get(targetCount + 2).squad
                        )
                        setImageAndText(
                            list.get(targetCount + 2).name,
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            getDrawableFromString(list.get(targetCount + 2).photo), bor = 0
                        )
                    })

                }

            }

        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun animateScore(scoreCount: Int) {
        AnimatedContent(
            targetState = scoreCount,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInVertically() + fadeIn() with
                            slideOutVertically() + fadeOut()
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInVertically() + fadeIn() with
                            slideOutVertically() + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }
        ) { targetCount ->
            Text(
                text = "Score : $targetCount",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                color = colorResource(id = R.color.yellow),
                style = MaterialTheme.typography.h1,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )


        }
    }

    @Composable
    private fun setSubtitle() {
        Text(
            text = "The first one to reach -5 loses",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            color = colorResource(id = R.color.lightGrey),
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }

    @Composable
    private fun setBoldText() {
        Text(
            text = ("Welcome to \n THE SQUAD GAMES!"),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            color = colorResource(id = R.color.white),
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    }

    @Composable
    private fun getDrawableFromString(drawableName: String): Int {
        val context = LocalContext.current

        if (drawableName == null) {
            return 0
        }
        val id: Int = context.getResources()
            .getIdentifier(drawableName.split(".")[0], "drawable", context.getPackageName())


        return id
    }

    private @Composable
    fun ContentIcon() {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        LottieAnimation(
            composition, modifier = Modifier
                .height(100.dp)
            // .background(colorResource(id = R.color.lightGrey))
        )

    }


    private @Composable
    fun Expanded(isRight: Boolean) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.flip))
        Column(
            modifier = Modifier
                // .background(colorResource(id = R.color.lightGrey))
                .padding(vertical = 8.dp)
                .height(80.dp)
        ) {
            if (isRight) {
                LottieAnimation(
                    composition
                )


            } else {
                val compositionWrong by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.sad))

                LottieAnimation(
                    compositionWrong
                )
            }

        }

    }

    fun getRandomNumber(min: Int, max: Int): Int {
        var x = (Math.random() * (max - min) + min).toInt()
        return x
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ComposothonTheme {
            loadSlotMachine()
        }
    }
}

