
package fr.istic.prg.list;


import fr.istic.prg.list_util.Iterator;
import fr.istic.prg.list_util.SuperT;

/**
 * Liste en double chaînage par références
 * 
 * @author Mickaël Foursov
 * @author Vincent Drevelle
 * 
 *         Version corrigée et instrumentée (compte du nombre d'opérations).
 *         Utilise un versionnage pour empêcher les modifs concurrentes.
 * 
 * @param <T> : le type de valeurs stockées
 */
public class List<T extends SuperT<T>> {

	/**
	 * Element de la liste
	 */
	private class Element {
		private T value;
		private Element left;
		private Element right;

		public Element() {
			value = null;
			left = null;
			right = null;
		}
	}

	/**
	 * Itérateur sur la liste
	 */
	public class ListIterator implements Iterator<T> {

		/** L'élément de la liste pointé par l'itérateur */
		private Element current;

		/**
		 * Contructeur privé. Place l'itérateur sur la tête de la liste.
		 * L'itérateur doit être construit par la liste
		 * 
		 * @see List::iterator()
		 */
		private ListIterator() {
			current = flag.right;
		}

		@Override
		public void goForward() {
			current = current.right;
		}

		@Override
		public void goBackward() {
			current = current.left;
		}

		@Override
		public void restart() {
			current = flag.right;
		}

		@Override
		public boolean isOnFlag() {
			return current == flag;
		}

		@Override
		public void remove() {
			assert current != flag : "impossible de retirer le drapeau";
			current.right.left = current.left;
			current.left.right = current.right;
			current = current.right;
		}

		@Override
		public T getValue() {
			return current.value;
		}

		@Override
		public T nextValue() {
			this.goForward();
			return this.getValue();
		}

		@Override
		public void addLeft(T value) {
			Element newValue = new Element();
			newValue.value = value;
			newValue.left = current.left;
			newValue.right = current;
			current.left.right = newValue;
			current.left = newValue;
			current = newValue;
		}

		@Override
		public void addRight(T value) {
			Element newValue = new Element();
			newValue.value = value;
			newValue.left = current;
			newValue.right = current.right;
			current.right.left = newValue;
			current.right = newValue;
			current = newValue;
		}

		@Override
		public void setValue(T value) {
			current.value = value;
		}

		@Override
		public String toString() {
			return "Iterateur de liste : pas d'affichage possible \n";
		}

	} // class ListIterator

	/** Le drapeau (sentinelle) */
	private Element flag;

	/**
	 * Constructeur
	 * 
	 * Instancie une liste vide (contenant seulement le drapeau).
	 */
	public List() {
		flag = new Element();
		flag.right = flag;
		flag.left = flag;
	}

	/**
	 * @return un itérateur sur la liste, l’élément courant de l’itérateur est
	 *         positionné sur l’élément de tête.
	 */
	public ListIterator iterator() {
		return new ListIterator();
	}

	/**
	 * @return true si la liste est vide, false sinon
	 */
	public boolean isEmpty() {
		return flag.right == flag && flag.left == flag;
	}

	/**
	 * Supprimer toutes les valeurs de la liste.
	 */
	public void clear() {
		flag = new Element();
		flag.right = flag;
		flag.left = flag;
	}

	/**
	 * Affecter la valeur v au drapeau.
	 * 
	 * @param v valeur à mettre dans le drapeau.
	 */
	public void setFlag(T v) {
		flag.value = v;
	}

	/**
	 * Ajouter v en tête de la liste.
	 * 
	 * @param v valeur à ajouter
	 */
	public void addHead(T v) {
		Element newValue = new Element();
		newValue.value = v;
		newValue.left = flag;
		newValue.right = flag.right;
		flag.right.left = newValue;
		flag.right = newValue;
	}

	/**
	 * Ajouter v en queue de la liste.
	 * 
	 * @param v valeur à ajouter
	 */
	public void addTail(T v) {
		Element newValue = new Element();
		newValue.value = v;
		newValue.left = flag.left;
		newValue.right = flag;
		flag.left.right = newValue;
		flag.left = newValue;
	}

	/**
	 * @return une copie profonde de la liste this.
	 */
	public List<T> copyOf() {
		List<T> nouvelleListe = new List<>();
		ListIterator it = iterator();
		while (!it.isOnFlag()) {
			nouvelleListe.addTail(it.getValue().copyOf());
			// UNE COPIE EST NECESSAIRE !!!
			it.goForward();
		}
		return nouvelleListe;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("contenu de la liste : \n");
		ListIterator it = iterator();
		while (!it.isOnFlag()) {
			result.append(it.getValue().toString());
			result.append(" ");
			it.goForward();
		}
		return result.toString();
	}

}