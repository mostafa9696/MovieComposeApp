package com.example.moviecomposeapp.presentation.profile

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviecomposeapp.R
import com.example.moviecomposeapp.common.component.AnimatedAsyncImage
import com.example.moviecomposeapp.common.component.AppScaffold
import com.example.moviecomposeapp.common.component.FullScreenCircularProgressIndicator
import com.example.moviecomposeapp.common.component.ShowUserMessage
import com.example.moviecomposeapp.common.constants.ComponentDimens
import com.example.moviecomposeapp.common.constants.Dimens
import com.example.moviecomposeapp.navigaton.HomeSections
import com.example.moviecomposeapp.style.backgroundDark
import com.example.moviecomposeapp.style.backgroundLight
import com.example.moviecomposeapp.style.primaryContainerDark
import com.example.moviecomposeapp.style.primaryContainerLight
import com.example.moviecomposeapp.style.primaryDark
import com.example.moviecomposeapp.style.primaryLight
import com.example.moviecomposeapp.ui.MovieNavigationBar
import java.util.Locale

private enum class Languages {
    ENGLISH,
    TURKISH
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToRoute: (String) -> Unit,
    onLogOutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current


    if (uiState.userMessages.isNotEmpty()) {
        ShowUserMessage(
            message = uiState.userMessages.first().asString(),
            consumedMessage = viewModel::consumedUserMessage
        )
    }

    AppScaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = remember {
            {
                MovieNavigationBar(
                    tabs = HomeSections.values(),
                    currentRoute = HomeSections.PROFILE.route,
                    navigateToRoute = onNavigateToRoute
                )
            }
        }
    ) { paddingValues ->
        ProfileScreenContent(
            modifier = Modifier
                .padding(paddingValues),
            onLogOutClick = remember {
                {
                    onLogOutClick()
                }
            },
            profileImageUrl = uiState.profileImgUri?.toString() ?: "",
            userEmail = uiState.userEmail,
            onDarkThemeSwitchChange = remember { viewModel::setTheme },
            onLanguageSelect = remember {
                { selectedLanguage ->
                }
            },
            isAppThemeDark = uiState.isDarkModeOn,
            onDynamicColorSwitchChange = remember { viewModel::setDynamicColor },
            isDynamicColorActive = uiState.isDynamicColorOn,
            onPickUpFromGalleryClick = remember {
                {}
            },
            isProfileImageUploading = uiState.isProfileImageUploading,
            currentLanguage = Locale.getDefault().displayLanguage
        )
    }
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier,
    onLogOutClick: () -> Unit,
    profileImageUrl: String,
    userEmail: String,
    onDarkThemeSwitchChange: (Boolean) -> Unit,
    onLanguageSelect: (Languages) -> Unit,
    isAppThemeDark: Boolean,
    onDynamicColorSwitchChange: (Boolean) -> Unit,
    isDynamicColorActive: Boolean,
    onPickUpFromGalleryClick: () -> Unit,
    isProfileImageUploading: Boolean,
    currentLanguage: String,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileSection(
            modifier = Modifier.weight(2f),
            onLogOutClick = onLogOutClick,
            profileImageUrl = profileImageUrl,
            userEmail = userEmail,
            isAppThemeDark = isAppThemeDark,
            onPickUpFromGalleryClick = onPickUpFromGalleryClick,
            isProfileImageUploading = isProfileImageUploading
        )
        SettingsSection(
            modifier = Modifier.weight(3f),
            onDarkThemeSwitchChange = onDarkThemeSwitchChange,
            onLanguageSelect = onLanguageSelect,
            isAppThemeDark = isAppThemeDark,
            onDynamicColorSwitchChange = onDynamicColorSwitchChange,
            isDynamicColorActive = isDynamicColorActive,
            currentLanguage = currentLanguage
        )
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier,
    onLogOutClick: () -> Unit,
    profileImageUrl: String,
    userEmail: String,
    isAppThemeDark: Boolean,
    onPickUpFromGalleryClick: () -> Unit,
    isProfileImageUploading: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    if (isAppThemeDark) {
                        listOf(primaryDark, primaryContainerDark, backgroundDark)
                    } else {
                        listOf(primaryLight, primaryContainerLight, backgroundLight)
                    }
                )
            )
    ) {
        TopAppBar(onLogOutClick = onLogOutClick)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(ComponentDimens.profileImageSize),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (isProfileImageUploading) {
                    FullScreenCircularProgressIndicator()
                } else {
                    AnimatedAsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        imageUrl = profileImageUrl,
                        borderShape = CircleShape,
                        borderStroke = BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF00C3FF), Color(0xFFFFFF1C))
                            )
                        ),
                        errorImageDrawableId = R.drawable.blank_profile_image
                    )
                    IconButton(
                        modifier = Modifier.size(48.dp),
                        onClick = onPickUpFromGalleryClick,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Icon(imageVector = Icons.Filled.Image, contentDescription = null)
                    }
                }
            }
            Text(
                modifier = Modifier.padding(top = Dimens.twoLevelPadding),
                text = userEmail,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier,
    onDarkThemeSwitchChange: (Boolean) -> Unit,
    onLanguageSelect: (Languages) -> Unit,
    isAppThemeDark: Boolean,
    onDynamicColorSwitchChange: (Boolean) -> Unit,
    isDynamicColorActive: Boolean,
    currentLanguage: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.twoLevelPadding)
            .padding(top = Dimens.twoLevelPadding),
    ) {
        Text(
            text = "Seetings",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.twoLevelPadding)
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DarkThemePicker(
                onDarkThemeSwitchChange = onDarkThemeSwitchChange,
                isAppThemeDark = isAppThemeDark
            )
            LanguagePicker(onLanguageSelect = onLanguageSelect, currentLanguage = currentLanguage)
            if (Build.VERSION.SDK_INT >= 31) {
                DynamicColorPicker(
                    onDynamicColorSwitchChange = onDynamicColorSwitchChange,
                    isDynamicColorActive = isDynamicColorActive
                )
            }
            Spacer(modifier = Modifier.height(Dimens.twoLevelPadding))
            Text(
                text = "App by Mostafa Hussien",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun DarkThemePicker(onDarkThemeSwitchChange: (Boolean) -> Unit, isAppThemeDark: Boolean) {
    SettingItem {
        Text(text = "Dark theme")
        Switch(
            checked = isAppThemeDark,
            onCheckedChange = {
                onDarkThemeSwitchChange(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagePicker(onLanguageSelect: (Languages) -> Unit, currentLanguage: String) {
    SettingItem(height = 64.dp) {
        var expanded by remember { mutableStateOf(false) }

        Text(text = "Languages")
        Box {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currentLanguage)
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = {
                        Text(text = "English")
                    },
                    onClick = {
                        onLanguageSelect(Languages.ENGLISH)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "Arabic")
                    },
                    onClick = {
                        onLanguageSelect(Languages.TURKISH)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DynamicColorPicker(
    onDynamicColorSwitchChange: (Boolean) -> Unit,
    isDynamicColorActive: Boolean,
) {
    var checked by remember { mutableStateOf(isDynamicColorActive) }

    SettingItem {
        Text(text = "Dynamic color")
        Switch(
            checked = checked,
            onCheckedChange = {
                onDynamicColorSwitchChange(it)
                checked = !checked
            }
        )
    }
}

@Composable
private fun SettingItem(height: Dp = 48.dp, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}


@Composable
private fun TopAppBar(onLogOutClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = {}) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = null, tint = Color.Black)
        }
        IconButton(onClick = onLogOutClick) {
            Icon(imageVector = Icons.Filled.Logout, contentDescription = null, tint = Color.Black)
        }
    }
}