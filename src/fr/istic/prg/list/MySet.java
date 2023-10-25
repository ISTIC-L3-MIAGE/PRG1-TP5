package fr.istic.prg.list;

import java.io.FileInputStream;

import fr.istic.prg.list_util.Comparison;
import fr.istic.prg.list_util.Iterator;
import fr.istic.prg.list_util.List;
import fr.istic.prg.list_util.SmallSet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * @author Antonella Atterey <antonella.atterey@etudiant.univ-rennes1.fr>
 * @author Ezan Tahi <ezan.tahi@etudiant.univ-rennes1.fr>
 * @class L3 MIAGE 2023/2024
 */

public class MySet extends List<SubSet> {

	/**
	 * Borne supérieure pour les rangs des sous-ensembles.
	 */
	private static final int MAX_RANG = 128;
	/**
	 * Sous-ensemble de rang maximal à mettre dans le drapeau de la liste.
	 */
	private static final SubSet FLAG_VALUE = new SubSet(MAX_RANG, new SmallSet());

	private static final String NEW_VALUE = " nouveau contenu :"; // demande de SonarLint

	/**
	 * Entrée standard.
	 */
	private static final Scanner standardInput = new Scanner(System.in);

	public MySet() {
		super();
		setFlag(FLAG_VALUE);
	}

	/**
	 * Fermer tout (actuellement juste l'entrée standard).
	 */
	public static void closeAll() {
		standardInput.close();
	}

	private static Comparison compare(int a, int b) {
		if (a < b) {
			return Comparison.INF;
		} else if (a == b) {
			return Comparison.EGAL;
		} else {
			return Comparison.SUP;
		}
	}

	/**
	 * Afficher à l’écran les entiers appartenant à this, dix entiers par ligne
	 * d’écran.
	 */
	public void print() {
		System.out.println(" [version corrigee de contenu]");
		this.print(System.out);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////////// Appartenance, Ajout, Suppression, Cardinal
	// ////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * @return true si le nombre saisi par l'utilisateur appartient à this, false
	 *         sinon
	 */
	public boolean contains() {
		System.out.println(" valeur cherchee : ");
		int value = readValue(standardInput, 0);
		return this.containsValue(value);
	}

	/**
	 * @param element valeur à tester
	 * @return true si valeur appartient à l'ensemble, false sinon
	 */

	public boolean containsValue(int value) {
		int rank = value / 256;
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
	 * Ajouter à this toutes les valeurs saisies par l'utilisateur et afficher le
	 * nouveau contenu (arrêt par lecture de -1).
	 */
	public void add() {
		System.out.println(" valeurs a ajouter (-1 pour finir) : ");
		this.addAllFromStream(System.in);
		this.printNewState();
	}

	/**
	 * Ajouter à this toutes les valeurs prises dans is.
	 * 
	 * @param is flux d'entrée.
	 */
	public void addAllFromStream(InputStream is) {
		try {
			int number = is.read();
			while (number != -1) {
				this.addNumber(number);
				number = is.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ATTENTION: L'un des nombres saisi n'est pas un entier.");
		}
	}

	/**
	 * Ajouter element à this.
	 *
	 * @param element valuer à ajouter.
	 */
	public void addNumber(int value) {
		Iterator<SubSet> it = iterator();
		// Parcours et ajout
		int rank = value / 256;
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
	}

	/**
	 * Supprimer de this toutes les valeurs saisies par l'utilisateur et afficher le
	 * nouveau contenu (arrêt par lecture de -1).
	 */
	public void remove() {
		System.out.println("  valeurs a supprimer (-1 pour finir) : ");
		this.removeAllFromStream(System.in);
		this.printNewState();
	}

	/**
	 * Supprimer de this toutes les valeurs prises dans is.
	 * 
	 * @param is flux d'entrée
	 */
	public void removeAllFromStream(InputStream is) {
		try {
			int number = is.read();
			while (number != -1) {
				this.removeNumber(number);
				number = is.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ATTENTION: L'un des nombres saisi n'est pas un entier.");
		}
	}

	/**
	 * Supprimer element de this.
	 * 
	 * @param element valeur à supprimer
	 */
	public void removeNumber(int value) {
		Iterator<SubSet> it = iterator();
		// Parcours et suppression
		int rank = value / 256;
		if (0 <= rank && rank < MAX_RANG) {
			while (it.getValue().rank < rank) {
				it.goForward();
			}
			SubSet sub = it.getValue();
			if (sub.rank == rank) {
				sub.set.remove(rank%256);
				// Suppression des SubSet vides
				if (sub.set.isEmpty()) {
					it.remove();
				}
			}
		}
	}

	/**
	 * @return taille de l'ensemble this
	 */
	public int size() {
		Iterator<SubSet> it = iterator();
		int size = 0;
		
		while (!it.isOnFlag()) {
			size += it.getValue().set.size();
			it.goForward();
		}
		return size;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////// Difference, DifferenceSymetrique, Intersection, Union ///////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * This devient la différence de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void difference(MySet set2) {
		Iterator<SubSet> it1 = iterator();
		Iterator<SubSet> it2 = set2.iterator();
		
		while (!it1.isOnFlag() && !it2.isOnFlag()) {
			SubSet sub1 = it1.getValue();
			SubSet sub2 = it2.getValue();
			if (sub1.rank < sub2.rank) {
				it1.goForward();
			} else if (sub1.rank > sub2.rank) {
				it2.goForward();
			} else {
				sub1.set.difference(sub2.set);
				it1.goForward();
				it2.goForward();
			}
		}
	}

	/**
	 * This devient la différence symétrique de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void symmetricDifference(MySet set2) {
		Iterator<SubSet> it1 = iterator();
		Iterator<SubSet> it2 = set2.iterator();
		
		while (!it1.isOnFlag() && !it2.isOnFlag()) {
			SubSet sub1 = it1.getValue();
			SubSet sub2 = it2.getValue();
			if (sub1.rank < sub2.rank) {
				it1.goForward();
			} else if (sub1.rank > sub2.rank) {
				it1.addLeft(sub2);
				it2.goForward();
			} else {
				sub1.set.symmetricDifference(sub2.set);
				it1.goForward();
				it2.goForward();
			}
		}
	}

	/**
	 * This devient l'intersection de this et set2.
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

	/**
	 * This devient l'union de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void union(MySet set2) {
		Iterator<SubSet> it1 = iterator();
		Iterator<SubSet> it2 = set2.iterator();
		
		while (!it1.isOnFlag() && !it2.isOnFlag()) {
			SubSet sub1 = it1.getValue();
			SubSet sub2 = it2.getValue();
			if (sub1.rank > sub2.rank) {
				it1.addLeft(it2.getValue());
				it2.goForward();
			} else {
				sub1.set.union(sub2.set);
				it1.goForward();
				it2.goForward();
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////// Egalit�, Inclusion ////////////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * @param o deuxième ensemble
	 * 
	 * @return true si les ensembles this et o sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (!(o instanceof MySet)) {
			return false;
		}
		
		MySet set2 = (MySet) o;
		Iterator<SubSet> it1 = iterator();
		Iterator<SubSet> it2 = set2.iterator();
		
		while (!it1.isOnFlag() && !it2.isOnFlag()) {
			SubSet sub1 = it1.getValue();
			SubSet sub2 = it2.getValue();
			if (!sub1.equals(sub2)) {
				return false;
			}
			it1.goForward();
			it2.goForward();
		}
		return true;
	}

	/**
	 * @param set2 deuxième ensemble
	 * @return true si this est inclus dans set2, false sinon
	 */
	public boolean isIncludedIn(MySet set2) {
		Iterator<SubSet> it2 = set2.iterator();
		
		while (!it2.isOnFlag()) {
			SubSet sub2 = it2.getValue();
			for (int i = 0; i < 256; i++) {
				int x = (256*sub2.rank) + i;
				if (sub2.set.contains(i) && !this.containsValue(x)) {
					return false;
				}
			}
			it2.goForward();
		}
		return true;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// //////// Rangs, Restauration, Sauvegarde, Affichage //////////////
	// /////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() {
		int i = 1;
		int result = 0;
		Iterator<SubSet> it = this.iterator();
		while (!it.isOnFlag()) {
			SubSet ss = it.getValue();
			result += i * ss.rank * ss.set.hashCode();
			i *= 31;
		}
		return result;
	}

	/**
	 * Afficher les rangs présents dans this.
	 */
	public void printRanks() {
		System.out.println(" [version corrigee de rangs]");
		this.printRanksAux();
	}

	private void printRanksAux() {
		int count = 0;
		Iterator<SubSet> it = this.iterator();
		StringBuilder line = new StringBuilder("Rangs presents : ");
		while (!it.isOnFlag()) {
			line.append(it.getValue().rank + "  ");
			count = count + 1;
			if (count == 10) {
				line.append("\n");
				count = 0;
			}
			it.goForward();

		}
		System.out.println(line.toString());
		if (count > 0) {
			System.out.println("\n");
		}
	}

	/**
	 * Créer this à partir d’un fichier choisi par l’utilisateur contenant une
	 * séquence d’entiers positifs terminée par -1 (cf f0.ens, f1.ens, f2.ens,
	 * f3.ens et f4.ens).
	 */
	public void restore() {
		String fileName = readFileName();
		InputStream inFile;
		try {
			inFile = new FileInputStream(fileName);
			System.out.println(" [version corrigee de restauration]");
			this.clear();
			this.addAllFromStream(inFile);
			inFile.close();
			System.out.println(NEW_VALUE);
			this.printNewState();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("fichier " + fileName + " inexistant");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme de fermeture du fichier " + fileName);
		}
	}

	/**
	 * Sauvegarder this dans un fichier d’entiers positifs terminé par -1.
	 */
	public void save() {
		System.out.println(" [version corrigee de sauvegarde]");
		OutputStream outFile;
		try {
			outFile = new FileOutputStream(readFileName());
			this.print(outFile);
			outFile.write("-1\n".getBytes());
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("pb ouverture fichier lors de la sauvegarde");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme de fermeture du fichier");
		}
	}

	/**
	 * @return l'ensemble this sous forme de chaîne de caractères.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int count = 0;
		SubSet subSet;
		int startValue;
		Iterator<SubSet> it = this.iterator();
		while (!it.isOnFlag()) {
			subSet = it.getValue();
			startValue = subSet.rank * 256;
			for (int i = 0; i < 256; ++i) {
				if (subSet.set.contains(i)) {
					StringBuilder number = new StringBuilder(String.valueOf(startValue + i));
					int numberLength = number.length();
					for (int j = 6; j > numberLength; --j) {
						number.append(" ");
					}
					result.append(number);
					++count;
					if (count == 10) {
						result.append("\n");
						count = 0;
					}
				}
			}
			it.goForward();
		}
		if (count > 0) {
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Imprimer this dans outFile.
	 *
	 * @param outFile flux de sortie
	 */
	private void print(OutputStream outFile) {
		try {
			String string = this.toString();
			outFile.write(string.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Afficher l'ensemble avec sa taille et les rangs présents.
	 */
	private void printNewState() {
		this.print(System.out);
		int size = this.size();
		System.out.println("Nombre d'elements : " + size);
		this.printRanksAux();
	}

	/**
	 * @param scanner
	 * @param min     valeur minimale possible
	 * @return l'entier lu au clavier (doit être entre min et 32767)
	 */
	private static int readValue(Scanner scanner, int min) {
		int value = scanner.nextInt();
		while (value < min || value > 32767) {
			System.out.println("valeur incorrecte");
		}
		return value;
	}

	/**
	 * @return nom de fichier saisi psar l'utilisateur
	 */
	private static String readFileName() {
		System.out.println(" nom du fichier : ");
		return standardInput.next();
	}
}