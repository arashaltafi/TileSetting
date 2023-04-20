package com.arash.altafi.tilesetting

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast

class TestTile : TileService() {

    override fun onClick() {
        super.onClick()
        val tile = qsTile // get Instance.
        when (tile.state) {
            Tile.STATE_ACTIVE -> tile.state = Tile.STATE_INACTIVE
            Tile.STATE_INACTIVE -> tile.state = Tile.STATE_ACTIVE
            else -> {}
        }
        Toast.makeText(this, "onClick: tile.state: " + tile.state, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onClick: tile.state: " + tile.state)
        tile.updateTile()
    }

    override fun onTileAdded() {
        super.onTileAdded()
        Log.d(TAG, "onTileAdded: Called when the user adds this tile to Quick Settings.")
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        Log.d(TAG, "onTileRemoved: Called when the user removes this tile from Quick Settings.")
    }

    override fun onStartListening() {
        super.onStartListening()
        Log.d(TAG, "onStartListening: Called when this tile moves into a listening state.")
    }

    override fun onStopListening() {
        super.onStopListening()
        Log.d(TAG, "onStopListening: Called when this tile moves out of the listening state.")
    }

    private companion object {
        const val TAG = "TestTile"
    }

}