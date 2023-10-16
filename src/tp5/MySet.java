package tp5;

import java.util.Iterator;
import java.util.Scanner;

public class MySet { //extends List<SubSet> {
	private static final int MAX_RANG = 128;
	private static final SubSet FLAG_VALUE = new SubSet(MAX_RANG, new SmallSet());
	
	public MySet() {
		super();
		setFlag(FLAG_VALUE);
	}
	
	/**
	* @return true si le nombre saisi par l’utilisateur appartient à this,
	* false sinon
	*/
	public boolean contains() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Entrez un nombre: ");
		int rank = scan.nextInt() / 256;
		if (0 <= rank && rank < MAX_RANG) {
			Iterator<SubSet> it = iterator();
			while (it.getValue().rank < rank) {
				it.goForward();
			}
			SubSet sub = it.getValue();
			return (sub.rank == rank && sub.set.contains(rank%256));
		}
		return false;
	}
	
	/**
	* Ajouter à this toutes les valeurs saisies par l’utilisateur et
	* afficher le nouveau contenu (arrêt par lecture de -1).
	*/
	public boolean add() {
		Scanner scan = new Scanner(System.in);
		Iterator<SubSet> it = iterator();
		
		System.out.print("Entrez un nombre: ");
		int userNumber = scan.nextInt();
		
		while (userNumber != -1) {
			// Parcours et ajout
			int rank = userNumber / 256;
			if (0 <= rank && rank < MAX_RANG) {
				while (it.getValue().rank < rank) {
					it.goForward();
				}
				SubSet sub = it.getValue();
				if (sub.rank == rank) {
					sub.set.add(rank%256);
				} else {
					SmallSet set = new SmallSet();
					set.add(rank%256);
					it.addLeft(new SubSet(rank, set));
				}
			}
			// Affichage du nouveau contenu
			System.out.println("Nouveau contenu: " + toString());
			
			// Saisie d'un nouveau nombre
			it.restart();
			System.out.print("Entrez un autre nombre ou : ");
			userNumber = scan.nextInt();
		}
	}
	
	/**
	* this devient l’intersection de this et set2.
	*
	* @param set2 deuxième ensemble
	*/
	public void intersection(MySet set2) {
		Iterator<SubSet> it1 = iterator();
		Iterator<SubSet> it2 = set2.iterator();
		
		while (!it1.isOnFlag() && !it2.isOnFlag()) {
			SubSet sub1 = it1.getValue();
			SubSet sub2 = it2.getValue();
			if (sub1.rank < sub2.rank) {
				it1.remove();
			} else if (sub1.rank > sub2.rank) {
				it2.goForward();
			} else {
				sub1.set.intersection(sub2.set);
				it1.goForward();
				it2.goForward();
			}
		}
	}
}
