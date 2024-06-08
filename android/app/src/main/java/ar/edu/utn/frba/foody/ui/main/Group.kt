package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataClasses.*
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun GroupScreen(
    navController: NavController,
    orderViewModel: OrderViewModel,
    groupViewModel: GroupViewModel
) {
    val group = groupViewModel.getPickedGroup()

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroup(navController = navController) }
    ) {
        Box(
            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.8f)
                )
                Text(
                    text = group.members.size.toString() + "/" + group.limit.toString(),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(0.2f)
                )
            }
        }
        Box(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(515.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(group.members.size) { index ->
                    UserRow(
                        user = group.members[index],
                        orderViewModel,
                        groupViewModel = groupViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun UserRow(user: User, orderViewModel: OrderViewModel, groupViewModel: GroupViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(0.8f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.rouned_user_icon),
                contentDescription = "User Icon",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center
            )
            Text(
                text = user.userName,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(0.2f)) {
            IconButton(
                onClick = {
                    groupViewModel.deleteUser(user);
                    orderViewModel.updateGroup(groupViewModel.getPickedGroup())
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cross_icon),
                    contentDescription = "Cross Icon",
                    modifier = Modifier.size(16.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun TopGroup(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Cart_Screen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.go_back),
                    contentDescription = "Go Back Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    )
}

@Preview
@Composable
fun GroupPreview() {
    val navController = rememberNavController()
    val orderViewModel = OrderViewModel()
    val groupViewModel = GroupViewModel()
    GroupScreen(
        navController = navController,
        orderViewModel = orderViewModel,
        groupViewModel = groupViewModel
    )
}