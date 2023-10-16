package tp5;

public class SmallSet {
	
	private int SET_SIZE = 256;
	private boolean[] tab = new boolean[SET_SIZE];
	
	public SmallSet() {
		for (int i = 0; i < SET_SIZE; ++i) {
			tab[i] = false;
		}
	}
	
	public SmallSet (boolean[] t) {
		for (int i = 0; i < SET_SIZE; ++i) {
			tab[i] = t[i];
		}
	}
	
	// Retourne le nombre d'éléments dans le tableau
	public int size() {
		int count = 0;
		for (int i = 0; i < SET_SIZE; ++i) {
			if (tab[i]) {
				count++;
			}
		}
		return count;
		
	}
	
	// Vérifie si x appartient à l'ensemble
	
	/**
	 * 
	 * @param x valeur à tester
	 * @pre 0<=x<=255
	 * @return true si x appartient à l'ensemble, false sinon
	 */
	
	public boolean contains (int x) {
		return tab[x];
	}
	
	//verifie si le tableau est vide
	public boolean isEmpty() {
		for (int i = 0; i < SET_SIZE; ++i) {
			if (tab[i]) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * @param x valeur à ajouter
	 * @pre 0<=x<=255
	 */
	public void add(int x) {
		tab[x] = true;	
	}
	/**
	 * 
	 * @param x valeur à supprimer
	 * @pre 0<=x<=255
	 */
	public void remove(int x) {
		tab[x] = false;	
	}
	/**
	 * 
	 * ajoute à l'ensemble les valeurs deb, deb+1, deb+2,..., fin
	 * @param begin début de l'intervalle
	 * @param fin de l'intervalle
	 * @pre 0<=begin<=end<=255
	 */
	public void addInterval (int deb, int fin) {
		for (int i = deb; i < fin;++i) {
			tab[i] = true;
		}
	}
	/**
	 * 
	 * supprime de  l'ensemble les valeurs deb, deb+1, deb+2,..., fin
	 * @param begin début de l'intervalle
	 * @param fin de l'intervalle
	 * @pre 0<=begin<=end<=255
	 */
	public void removeInterval (int deb, int fin) {
		for (int i = deb; i < fin; ++i) {
			tab[i] = false;
		}
	}
	/**
	 * réalise l'opération this union set2
	 * @param set2
	 */
	public void union(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; ++i) {
			if(set2.tab[i]) {
				this.tab[i] = true;
			}
		}
	}
	
	/**
	 * réalise l'opération this inter set2
	 * @param set2
	 */
	//les elements communs aux deux ensembles
	public void intersection (SmallSet set2) {
		for (int i = 0; i < SET_SIZE; ++i) {
			if(!set2.tab[i]) {
				this.tab[i] = false;
			}
		}
		
		
	}
	
	
	// l'ensemble des éléments de this qui ne sont pas dans set2
	public void difference(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; ++i) {
			if (set2.tab[i]) {
				this.tab[i] = false;
			}
		}
		
	}
	//les elements qui sont presents dans l'un et pas dans l'autre
	
	public void symmetricDifference(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; ++i) {
			if (set2.tab[i]) {
				this.tab[i] = !this.tab[i];
			}
		}
		
	}
	//inversion de la valeur de chaque élément dans le tableau
	public void complement() {
		for (int i = 0; i < SET_SIZE; ++i) {
			this.tab[i] = !this.tab[i];
		}
		
	}
	//vider le tableau
	public void clear() {
		for (int i = 0; i < SET_SIZE; ++i) {
			this.tab[i] = false;
		}
	}
	/**
	 * @param set2 second ensemble
	 * @return true si this est inclus dans set2, false sinon
	 */
	public boolean isIncludeIn(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; ++i) {
			if (this.tab[i] && !set2.tab[i]) {
				return false;
			}
		}
		return true;
		
		
	}
	// retourne qui copie de tab
	public SmallSet clone() {
		return new SmallSet(this.tab);
	}
	
	//verifie si this et set2 sont égaux et retourne false sinon
	@Override
	public boolean equals(Object set2) {
		if (this == set2) {
	        return true;
	    }
	    if (set2 == null || getClass() != set2.getClass()) {
	        return false;
	    }
	    SmallSet otherSet = (SmallSet) set2;
	    for (int i = 0; i <= 255; ++i) {
	        if (this.tab[i] != otherSet.tab[i]) {
	            return false;
	        }
	    }
	    return true;
		
	}
	
	public String toString() {
		String s = "éléments présents: ";
		for (int i = 0; i<=255; ++i) {
			if (tab[i]) {
				s = s + i + " ";
			}
		}
		return s;
	}
	
	
}