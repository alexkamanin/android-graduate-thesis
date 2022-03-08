@Suppress("DSL_SCOPE_VIOLATION")
plugins {
	id(libs.plugins.android.library.get().pluginId)
	id(libs.plugins.kotlin.android.get().pluginId)
	id(libs.plugins.hilt.android.get().pluginId)
	id(libs.plugins.kotlin.kapt.get().pluginId)
}

dependencies {
	implementation(libs.android.core)
	implementation(libs.android.appcompat)
	implementation(libs.android.material)
	implementation(libs.android.constraintlayout)
	implementation(libs.android.navigation.fragment)
	implementation(libs.android.swipe.refresh)

	implementation(libs.viewbinding)
	implementation(libs.bundles.kotlin.coroutines)

	implementation(libs.hilt.android)
	kapt(libs.hilt.compiler)

	implementation(projects.component.navigation)
	implementation(projects.component.core)
	implementation(projects.component.ui)

	implementation(projects.shared.exam)
	implementation(projects.shared.session)
}