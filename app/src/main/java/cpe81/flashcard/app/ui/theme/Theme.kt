import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import cpe81.flashcard.app.ui.theme.AppColors

private val LightColorScheme = lightColorScheme(
    primary = AppColors.PurplePrimary,
    onPrimary = AppColors.White,
    primaryContainer = AppColors.purpleMid,
    onPrimaryContainer = AppColors.PurpleDark,
    secondary = AppColors.TealAccent,
    onSecondary = AppColors.Black,
    background = AppColors.PurpleLight,
    onBackground = AppColors.GrayText,
    surface = AppColors.White,
    onSurface = AppColors.GrayText,
    error = AppColors.Error,
    onError = AppColors.White
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.PurpleDark,
    onPrimary = AppColors.White,
    primaryContainer = AppColors.PurplePrimary,
    onPrimaryContainer = AppColors.PurpleLight,
    secondary = AppColors.TealAccent,
    onSecondary = AppColors.Black,
    background = AppColors.Black,
    onBackground = AppColors.White,
    surface = AppColors.GrayText,
    onSurface = AppColors.White,
    error = AppColors.Error,
    onError = AppColors.Black
)

@Composable
fun ModernPurpleAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}