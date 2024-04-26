package com.example.triptracker.view

/**
 * A top bar for the navigation.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param title The title of the top bar.
 * @param canNavigateBack Whether the top bar should have a back button.
 * @param navigateUp The action to perform when the back button is clicked.
 * @param actions The actions to display on the top bar (like a save button).
 */
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun NavTopBar(
//    modifier: Modifier = Modifier,
//    title: String,
//    canNavigateBack: Boolean,
//    navigateUp: () -> Unit = {},
//    actions: @Composable () -> Unit = {}
// ) {
//  if (canNavigateBack) {
//    TopAppBar(
//        title = { Text(text = title) },
//        actions = { actions() },
//        navigationIcon = {
//          IconButton(onClick = navigateUp) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = md_theme_grey)
//          }
//        },
//        modifier = modifier)
//  } else {
//    TopAppBar(title = { Text(text = title) }, actions = { actions() }, modifier = modifier)
//  }
// }

/** Preview for the NavTopBar to have an example. */
// @Preview
// @Composable
// fun NavTopBarPreview() {
//  NavTopBar(
//      title = stringResource(id = R.string.app_name),
//      canNavigateBack = true,
//      navigateUp = {},
//      actions = {
//        FilledTonalButton(
//            onClick = {},
//            colors = ButtonDefaults.filledTonalButtonColors(containerColor = md_theme_orange),
//            modifier = Modifier.width(90.dp).height(30.dp),
//        ) {
//          Text(
//              text = "Save",
//              fontSize = 14.sp,
//              fontFamily = Montserrat,
//              fontWeight = FontWeight.SemiBold,
//              color = Color.White)
//        }
//      })
// }
