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

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.BehaviorSubject
import net.moisesborges.audioplayer.AudioPlayer
import net.moisesborges.audioplayer.PlaybackState
import net.moisesborges.base.BaseViewModeTest
import net.moisesborges.model.Station
import net.moisesborges.model.StreamUrl
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class AudioPlayerViewModelTest : BaseViewModeTest() {

    val playbackState = BehaviorSubject.create<PlaybackState>()

    val audioPlayer: AudioPlayer = mock {
        on { playbackState() } doReturn playbackState
    }

    lateinit var testSubject: AudioPlayerViewModel

    override fun setup() {
        testSubject = AudioPlayerViewModel(audioPlayer)
    }

    @Test
    fun `given playPause is called, when isEmpty, then it should be false`() {
        val station = givenStation("anyUrl")
        playbackState.onNext(PlaybackState(station, false))
        testSubject.playPause()

        val isEmpty = testSubject.isEmpty.extractValue()

        isEmpty `should be equal to` false
    }

    @Test fun `given playPause was not called, when isEmpty, then it should be true`() {
        val station = Station.EMPTY_STATION
        playbackState.onNext(PlaybackState(station, false))
        val isEmpty = testSubject.isEmpty.extractValue()

        isEmpty `should be equal to` true
    }

    @Test fun `given station is not loaded, when playPause, when it should not play or pause`() {
        val station = Station.EMPTY_STATION
        playbackState.onNext(PlaybackState(station, false))

        testSubject.playPause()

        verify(audioPlayer, never()).play()
        verify(audioPlayer, never()).pause()
    }

    @Test fun `given station is loaded and audio is playing, when playPause, when it should pause`() {
        val station = givenStation("anUrl")
        playbackState.onNext(PlaybackState(station, true))
        whenever(audioPlayer.isPlaying()).doReturn(true)

        testSubject.playPause()

        verify(audioPlayer).pause()
    }

    @Test fun `given station is loaded and audio is not playing, when playPause, when it should play`() {
        val station = givenStation("anUrl")
        playbackState.onNext(PlaybackState(station, false))
        whenever(audioPlayer.isPlaying()).doReturn(false)

        testSubject.playPause()

        verify(audioPlayer).play()
    }

    private fun givenStation(expectedStreamUrl: String): Station {
        return mock {
            on { streamUrl } doReturn StreamUrl(expectedStreamUrl)
        }
    }
}