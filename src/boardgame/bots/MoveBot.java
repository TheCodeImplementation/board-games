/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame.bots;

import boardgame.GameState;

public interface MoveBot {

        GameState getMove(GameState gameState, int maxDepth);
}
