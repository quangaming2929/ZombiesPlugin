package io.github.zap.zombiesplugin.events;

import io.github.zap.zombiesplugin.player.User;

public class UserJoinLeaveEventArgs extends EventArgs {
    public enum ChangeType {
        LEAVE,
        JOIN
    }

    public User changedUser;
    public ChangeType type;

    public UserJoinLeaveEventArgs(User user, ChangeType type) {
        changedUser = user;
        this.type = type;
    }
}
