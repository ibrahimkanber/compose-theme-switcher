package com.salsbyte.composetheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salsbyte.composetheme.preferences.AppTheme
import com.salsbyte.composetheme.preferences.UserSettings
import com.salsbyte.composetheme.ui.RadioButtonItem
import com.salsbyte.composetheme.ui.RadioGroupOptions
import com.salsbyte.composetheme.ui.theme.ComposeThemeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSettings: UserSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val theme = userSettings.themeStream.collectAsState()
            val useDarkColors = when (theme.value) {
                AppTheme.MODE_AUTO -> isSystemInDarkTheme()
                AppTheme.MODE_DAY -> false
                AppTheme.MODE_NIGHT -> true
            }

            ComposeThemeTheme(darkTheme = useDarkColors) {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ThemeSwitch(userSettings
                    ) {
                            theme -> userSettings.theme = theme
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeSwitch(
    userSettings: UserSettings,
    onItemSelected: (AppTheme) -> Unit
) {
    val theme = userSettings.themeStream.collectAsState()
    val themeItems = listOf(
        RadioButtonItem(
            id = AppTheme.MODE_DAY.ordinal,
            title = stringResource(id = R.string.light_theme),
        ),
        RadioButtonItem(
            id = AppTheme.MODE_NIGHT.ordinal,
            title = stringResource(id = R.string.dark_theme),
        ),
        RadioButtonItem(
            id = AppTheme.MODE_AUTO.ordinal,
            title = stringResource(id = R.string.auto_theme),
        ),
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Select theme",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp),
            color = MaterialTheme.colors.primary
        )
        RadioGroupOptions(
            items = themeItems,
            selected = theme.value.ordinal,
            onItemSelect = {
                    id -> onItemSelected(AppTheme.fromOrdinal(id))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeThemeTheme {
        //ThemeSwitch(userSettings)
    }
}