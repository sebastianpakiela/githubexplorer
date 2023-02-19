package com.sebastianpakiela.githubexplorer.feature.search

import com.sebastianpakiela.githubexplorer.base.Effect

sealed class SearchEffect : Effect

object ShowSnackbarEffect: SearchEffect()

