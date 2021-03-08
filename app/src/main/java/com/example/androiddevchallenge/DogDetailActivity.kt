package com.example.androiddevchallenge

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.model.Dog

const val SELECTED_DOG = "selected_dog"
const val SELECTED_POSITION = "selected_position"
const val ADOPTED = "adopted"

class DogDetailActivity:AppCompatActivity() {

    private lateinit var selectedDog: Dog
    private var selectedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dog = intent.getParcelableExtra<Dog>(SELECTED_DOG)
        selectedPosition = intent.getIntExtra(SELECTED_POSITION,0)
        Log.e("ymx", "onCreate: "+dog)
        if(dog == null){
            Toast.makeText(this,"There must be something wrong.",Toast.LENGTH_LONG).show()
            finish()
            return
        }
        selectedDog = dog
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = selectedDog.name
                            )
                        },
                        backgroundColor = Color.Transparent, elevation = 0.dp,
                        navigationIcon = {
                            IconButton(onClick = { navigateBack() }) {
                                val backIcon: Painter = painterResource(R.drawable.ic_back)
                                Icon(painter = backIcon, contentDescription = "ic_back")
                            }
                        }
                    )
                }
            ) {
                DisplayDogDetail(dog = selectedDog)
            }
        }

    }

    override fun onBackPressed() {
        navigateBack()
    }

    private fun navigateBack(){
        val intent = Intent()
        intent.putExtra(SELECTED_POSITION,selectedPosition)
        intent.putExtra(ADOPTED,selectedDog.adopted)
        setResult(RESULT_OK,intent)
        finish()
    }
}
var showConfirmDialog by mutableStateOf(false)


@Composable
fun DisplayDogDetail(dog: Dog){
    val stateDog by remember { mutableStateOf(dog)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DogAvatar(
            avatar = stateDog.avatarFilename,
            name = stateDog.name
        )
        Spacer(
            modifier = Modifier.requiredHeight(26.dp)
        )
        AdoptButton(
            adopted = stateDog.adopted
        )
        Spacer(
            modifier = Modifier.requiredHeight(26.dp)
        )
        DogIntroduction(
            introduction = stateDog.introduction
        )
    }
    if (showConfirmDialog) {
        AdoptConfirmDialog(dog = stateDog)
    }
}

@Composable
fun  DogAvatar(avatar: String, name: String){
    val imageIdentity = GlobalApp.context.resources.getIdentifier(
        avatar, "drawable",
        GlobalApp.context.packageName
    )
    val image: Painter = painterResource(imageIdentity)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = name,
            modifier = Modifier
                .requiredSize(150.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun AdoptButton(adopted: Boolean){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Button(onClick = { showConfirmDialog  = true },
        enabled = !adopted) {
            Text(text = if (adopted) "Adopted" else "Adopt")
        }
    }
}

@Composable
fun DogIntroduction(introduction: String) {
    Text(
        text = introduction,
        fontSize = 18.sp,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun AdoptConfirmDialog(dog: Dog){
    AlertDialog(
        onDismissRequest = {
            showConfirmDialog = false
        },
        text = {
            Text(
                text = "Do you want to adopt this lovely dog?",
                style = MaterialTheme.typography.body2
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    showConfirmDialog = false
                    dog.adopted = true
                }
            ) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showConfirmDialog = false
                }
            ) {
                Text(text = "No")
            }
        }
    )
}

