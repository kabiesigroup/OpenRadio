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

package net.moisesborges.ui.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.audioplayer.initialPlaybackState
import net.moisesborges.extensions.get
import net.moisesborges.extensions.plusAssign
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station

class AudioPlayerViewModel(private val audioPlayer: AudioPlayer) {

    private val disposables = CompositeDisposable()
    private val state: MutableLiveData<PlaybackState> = MutableLiveData<PlaybackState>().also {
        it.value = initialPlaybackState()
    }

    val title: LiveData<String> = Transformations.map(state) { it.station.name }
    val imageUrl: LiveData<ImageUrl> = Transformations.map(state) { it.station.imageUrl }
    val isPlaying: LiveData<Boolean> = Transformations.map(state) { it.isPlaying }
    val isEmpty: LiveData<Boolean> = Transformations.map(state) { it.station == Station.EMPTY_STATION }

    init {
        disposables += audioPlayer.playbackState()
            .subscribe { playbackState ->
                state.value = playbackState
            }
    }

    fun playPause() {
        val station = state.get().station
        if (station == Station.EMPTY_STATION) {
            return
        }

        if (audioPlayer.isPlaying()) {
            audioPlayer.pause()
        } else {
            audioPlayer.play()
        }
    }

    fun clear() {
        disposables.clear()
    }
}