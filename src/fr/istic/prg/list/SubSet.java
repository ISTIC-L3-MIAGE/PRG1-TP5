package fr.istic.prg.list;

import fr.istic.prg.list_util.SmallSet;
import fr.istic.prg.list_util.SuperT;

/**
 * Classe représentant les sous-ensembles de la classe MySet.
 * 
 * @author Antonella Atterey <antonella.atterey@etudiant.univ-rennes1.fr>
 * @author Ezan Tahi <ezan.tahi@etudiant.univ-rennes1.fr>
 * @class L3 MIAGE 2023/2024
 */

public class SubSet implements SuperT<SubSet> {

	public final int rank;
	public final SmallSet set;

	public SubSet() {
		rank = 0;
		set = new SmallSet();
	}

	public SubSet(int rank, SmallSet set) {
		this.rank = rank;
		this.set = set;
	}

	@Override
	public SubSet copyOf() {
		return new SubSet(rank, set.copyOf());
	}

	@Override
	public String toString() {
		return "Subset [rank=" + rank + ", set=" + set + "]";
	}

	@Override
	public int hashCode() {
		// pour respecter les bonnes pratiques
		final int prime = 31;
		int result = 1;
		result = prime * result + rank;
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SubSet)) {
			return false;
		}
		SubSet other = (SubSet) obj;
		return rank == other.rank && set.equals(other.set);
	}
}