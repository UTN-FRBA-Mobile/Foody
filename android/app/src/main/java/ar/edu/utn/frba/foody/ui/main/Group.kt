package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
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
                    text = group.groupId,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.8f)
                )
                Text(
                    text = group.members.size.toString() + "/" + group.membersLimit.toString(),
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
                text = if (user.admin) user.email + " (Admin)" else user.email,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        if (orderViewModel.user.admin) {
            if (user != orderViewModel.user) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(0.2f)) {
                    IconButton(
                        onClick = {
                            val updatedGroup = groupViewModel.deleteUser(user)
                            orderViewModel.updateGroup(updatedGroup)
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
                Spacer(modifier = Modifier.height(8.dp))
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


/*
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
 */