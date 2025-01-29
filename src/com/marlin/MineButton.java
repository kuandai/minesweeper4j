/**
 * @author Kuan Dai
 * 
 * Button representing mine
 */

package com.marlin;

import javax.swing.*;

public class MineButton extends JButton {
    private boolean isMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private int tilex;
    private int tiley;

    public MineButton(int x, int y) {
        this.tilex = x;
        this.tiley = y;
        this.isMine = false;
        this.isFlagged = false;
        this.isRevealed = false;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public int getTileX() {
        return tilex;
    }

    public int getTileY() {
        return tiley;
    }
}