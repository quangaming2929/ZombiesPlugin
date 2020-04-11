package io.github.zap.zombiesplugin.commands.mapeditor;

import io.github.zap.zombiesplugin.utils.Tuple;

public interface IEditorContext {
    Tuple<Boolean, String> canExecute(ContextManager session, String name, String[] args);
    void execute(ContextManager session, String name, String[] args);
}
