/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.moisesborges.features.di

import net.moisesborges.features.favorite.FavoriteStationManager
import net.moisesborges.features.location.LocationProvider
import net.moisesborges.features.recentsearches.RecentSearchManager
import net.moisesborges.features.recentsearches.RecentSearchRegistry
import net.moisesborges.features.recentsearches.RecentSearchReposity
import net.moisesborges.features.search.SearchEngine
import org.koin.dsl.module.module

val featuresModule = module {
    single { FavoriteStationManager(get()) }
    single { LocationProvider(get()) }
    single { SearchEngine(get()) }
    single { RecentSearchManager(get(), get()) }
    single<RecentSearchRegistry> { get<RecentSearchManager>() }
    single<RecentSearchReposity> { get<RecentSearchManager>() }
}