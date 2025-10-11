# KillC Plugin
A Minecraft plugin for Paper/Folia 1.21.10 that provides kill commands with configurable options.

## 🎇 Features
- **Single file implementation** - All functionality consolidated into one Java file
- **Folia compatible** - Fully supports Folia server software
- **Configurable** - Enable/disable plugin functionality via config
- **Multiple commands** - Separate commands for different use cases
- **Permission-based** - Proper permission system with op defaults
- **Tab completion** - Smart tab completion for player names and commands

## 🦺 Commands

- `/kill` - Kill yourself (no arguments) or another player
- `/kill <player>` - Kill a specific player (requires permission)
- `/kill reload` - Reload the plugin configuration (ops only)
- `/suicide` - Kill yourself (alternative to `/kill` with no arguments)

## 💎 Permissions

- `killc.use` - Basic permission to use kill commands (default: true)
- `killc.others` - Permission to kill other players (default: op)
- `killc.reload` - Permission to reload plugin config (default: op)
- `killc.*` - All permissions (default: op)

## 🎨 Configuration

The plugin creates a `config.yml` file with the following options:

```yaml
# Set to false to disable the plugin functionality
enabled: true
```

## 🔓 Compatibility

- **Minecraft Version**: 1.21.10
- **Server Software**: Paper, Folia
- **Java Version**: 21+
