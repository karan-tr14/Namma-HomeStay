package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "My Home", Icons.Default.Home)
    object Menu : Screen("menu", "Daily Menu", Icons.Default.Restaurant)
    object Inquiry : Screen("inquiry", "Inquiries", Icons.Default.Email)
    object Guide : Screen("guide", "Local Guide", Icons.Default.LocationOn)
}

@Composable
fun NammaHomeStayApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Menu.route) { MenuScreen() }
            composable(Screen.Inquiry.route) { InquiryScreen() }
            composable(Screen.Guide.route) { GuideScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Menu, Screen.Inquiry, Screen.Guide)
    NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("My Home Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Photo Gallery Placeholder
            Row(modifier = Modifier.fillMaxWidth().height(150.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PhotoBox("Room", Modifier.weight(1f))
                PhotoBox("Toilet", Modifier.weight(1f))
                PhotoBox("Farm", Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Rate", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("₹1,200 per night", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { /* Edit Rate */ }) {
                        Text("Update Rate")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Host Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Managed by: Smt. Lakshmi & Family")
            Text("Location: Coastal Karnataka (near Maravanthe)")
        }
    }
}

@Composable
fun PhotoBox(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.LightGray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.DarkGray)
            Text(label, fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

@Composable
fun MenuScreen() {
    var breakfast by remember { mutableStateOf("Akki Rotti with Coconut Chutney") }
    var dinner by remember { mutableStateOf("Bamboo Shoot Curry & Red Rice") }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Today's Menu", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Keep your guests excited about local food!", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = breakfast,
            onValueChange = { breakfast = it },
            label = { Text("Breakfast Special") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = dinner,
            onValueChange = { dinner = it },
            label = { Text("Dinner Special") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { /* Save Menu */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Post Today's Menu")
        }
    }
}

@Composable
fun InquiryScreen() {
    val inquiries = listOf(
        Inquiry("Rohan Smith", "Is the room available for coming Saturday?", "10:30 AM"),
        Inquiry("Priya Das", "Do you serve traditional fish curry?", "Yesterday"),
        Inquiry("Dr. Hegde", "We are 4 people, do you have extra mats?", "2 days ago")
    )
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inquiry Box", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(inquiries) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.name, fontWeight = FontWeight.Bold)
                            Text(item.time, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.message)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* Intent to call */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call Traveler")
                        }
                    }
                }
            }
        }
    }
}

data class Inquiry(val name: String, val message: String, val time: String)

@Composable
fun GuideScreen() {
    val spots = listOf(
        Spot("Blue Lagoon Creek", "5 mins walk - Best for sunset", Icons.Default.WbSunny),
        Spot("Ancient Banyan Tree", "1 km away - Great for meditation", Icons.Default.Nature),
        Spot("Local Pottery Shop", "In the village - Buy local art", Icons.Default.ShoppingBag)
    )
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Local Guide", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Help travelers find 'Secret Spots'", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(spots) { spot ->
                ListItem(
                    headlineContent = { Text(spot.name, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(spot.desc) },
                    leadingContent = { Icon(spot.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = { /* Add Spot */ }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Add New Secret Spot")
                }
            }
        }
    }
}

data class Spot(val name: String, val desc: String, val icon: ImageVector)
