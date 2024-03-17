/*
 *  Copyright (c) 2023 David Allison <davidallisongithub@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ichi2.anki.servicelayer

import com.google.android.material.snackbar.Snackbar
import com.ichi2.anki.AnkiActivity
import com.ichi2.anki.CollectionManager
import com.ichi2.anki.R
import com.ichi2.anki.snackbar.showSnackbar
import com.ichi2.libanki.MediaCheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun AnkiActivity.checkMedia(): MediaCheckResult? {
    return try {
        withContext(Dispatchers.IO) {
            if (ScopedStorageService.mediaMigrationIsInProgress(this@checkMedia)) {
                withContext(Dispatchers.Main) {
                    showSnackbar(
                        R.string.functionality_disabled_during_storage_migration,
                        Snackbar.LENGTH_SHORT
                    )
                }
                null
            } else {
                CollectionManager.withCol { media.check() }
            }
        }
    } finally {
        this@checkMedia.hideProgressBar()
    }
}
