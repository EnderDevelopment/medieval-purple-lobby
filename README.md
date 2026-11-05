# MedievalPurpleLobby Minecraft Plugin

MedievalPurpleLobby is a comprehensive Minecraft plugin that transforms your server with a custom medieval purple-themed lobby, complete with an integrated economy system, player shops, auction house, bounty hunting, and essential player commands. This all-in-one plugin gives server administrators powerful tools to create an engaging survival experience with a rich purple medieval aesthetic, featuring custom GUI icons, lobby NPCs, and a full suite of commands for homes, teleportation, kits, and moderation.

## Install

1. Download the latest MedievalPurpleLobby plugin JAR file.
2. Place the JAR file into your server's `plugins` folder.
3. Restart your server (or run `/reload confirm` if using Paper).
4. The plugin will generate its default configuration files and data folders on first run.

## Use

1. **Set up the hub:** Run `/accepthub` while standing at your desired lobby location (requires `endermedieval.admin.hub` permission). This sets the spawn point and spawns the Shop Keeper, Auctioneer, and Bounty Master NPCs.

2. **Economy system:** Players start with 100 coins automatically. Use `/balance` to check funds, `/pay <player> <amount>` to send money, and admin `/eco` commands (`give`, `take`, `set`, `reset`) to manage balances.

3. **Shopping:** Access the shop by clicking the chest in the hotbar or via the Shop Keeper NPC. Browse categories and click items to purchase them with your economy balance.

4. **Auction house:** Open `/auction` to view current listings. Sell items with `/auction sell <price>` while holding the item. Place bids with `/bid <auctionId> <amount>`.

5. **Bounties:** Place bounties with `/bounty set <player> <amount>`, view active bounties with `/bounty`, and claim them by killing the target player (automatic) or using `/bounty claim <player>`.

6. **Homes and commands:** Set up to unlimited homes with `/sethome <name>`, teleport with `/home <name>`, list all with `/homes`, and delete with `/delhome <name>`. Navigate using the compass (Warp Menu), bed (Return to Hub), and NPC markers.

7. **Kits:** Use `/kit <name>` to claim predefined kits (Starter, Miner, Farmer) with cooldowns. List all available kits with `/kits`.

8. **Admin tools:** Use `/fly`, `/god`, `/heal`, `/feed`, and `/gamemode <mode>` for player management (requires respective permissions).

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/5fc9c851-5b7b-4698-bb20-b954ef54dc58?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=5fc9c851-5b7b-4698-bb20-b954ef54dc58
