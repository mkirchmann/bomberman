package de.neuenberger.games.core.model;

/**
 * 
 * @author Michael Kirchmann
 *
 * @param <G>
 */
public interface Visitor<G> {
	void accept(G g);
}
