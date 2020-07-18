/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package boardgame;

public enum Colour {
    LIGHT, DARK;
    public Colour opponent() {
        return this == LIGHT ? DARK : LIGHT;
    }
}
