package com.firstJogo.estrutura;

public interface ExternalCallback {
public void KeyCallback(long window, int key, int scancode, int action, int mods);
public void CursorPosCallback();
}