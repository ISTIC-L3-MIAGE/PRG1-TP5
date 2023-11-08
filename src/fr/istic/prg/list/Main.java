package fr.istic.prg.list;

public class Main {
	
	static MySet mySet1 = new MySet();
	static MySet mySet2 = new MySet();
	
	public static void testAdd(int nb) {
		mySet1.addNumber(nb);
		System.out.println("\n");
		System.out.println("Ajout de " + nb);
		System.out.println("Set -> " + mySet1.toString());
		showSize();
	}
	
	public static void testRemove(int nb) {
		mySet1.removeNumber(nb);
		System.out.println("\n");
		System.out.println("Suppression de " + nb);
		System.out.println("Set -> " + mySet1.toString());
		showSize();
	}
	
	public static void testContains(int nb) {
		System.out.println(nb + " est dans le set ? " + mySet1.containsValue(nb));
	}
	
	public static void showSize() {
		System.out.println("Size: " + mySet1.size());
	}

	public static void main(String[] args) {
		int[] testValue = {100, 10000, 32000, 100};
		showSize();
		// On vérifie que l'ens est vide
		for (int i = 0; i < testValue.length; i++) {
			testContains(testValue[i]);
		}
		// On ajoute des valeurs pour tester
		for (int i = 0; i < testValue.length; i++) {
			testAdd(testValue[i]);
		}
		// On vérifie que les valeurs ont bien été ajoutées
		for (int i = 0; i < testValue.length; i++) {
			testContains(testValue[i]);
		}
		
		testRemove(100);
		testRemove(2);
		
		// On vérifie que les valeurs ont bien été ajoutées
		for (int i = 0; i < testValue.length; i++) {
			testContains(testValue[i]);
		}


	}

}
