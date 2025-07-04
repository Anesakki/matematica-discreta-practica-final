import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */

class Entrega {

    static final String[] NOMS = {"Jesús de Quiroga, Pablo Calafat Espín, Ignacio Jael Fernández Urreta "};

    /*
     * Aquí teniu els exercicis del Tema 1 (Lògica).
     */
    static class Tema1 {

        /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
         */
        static final char CONJ = '∧';
        static final char DISJ = '∨';
        static final char IMPL = '→';
        static final char NAND = '.';

        static int exercici1(char[] ops, int[] vars) {

            int nVars = Arrays.stream(vars).max().getAsInt() + 1;
            int totalComb = 1 << nVars;

            boolean sempreCerta = true;
            boolean sempreFalsa = true;

            for (int comb = 0; comb < totalComb; comb++) {  //Este for recorre todas las posibilidades de combinaciones
                boolean[] valors = new boolean[nVars];
                for (int i = 0; i < nVars; i++) {
                    valors[i] = ((comb >> i) & 1) == 1;
                }

                boolean resultat = valors[vars[0]];         //Se inicia en este valor y lo aumentamos mas tarde para recorrerlo todo
                for (int i = 0; i < ops.length; i++) {      //Este for evalua la expresion lógica paso a paso
                    boolean seguent = valors[vars[i + 1]];
                    switch (ops[i]) {
                        case CONJ:
                            resultat = resultat && seguent;
                            break;
                        case DISJ:
                            resultat = resultat || seguent;
                            break;
                        case IMPL:
                            resultat = !resultat || seguent;
                            break;
                        case NAND:
                            resultat = !(resultat && seguent);
                            break;
                    }
                }

                if (resultat) {
                    sempreFalsa = false;
                } else {
                    sempreCerta = false;
                }
            }

            if (sempreCerta) {
                return 1; //tautologia
            }
            if (sempreFalsa) {
                return 0; //contradicció
            }
            return -1;
        }

        /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
         */
        static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {

            boolean P = true; //P(x) es cierto
            int totalQ = 0;

            for (int x : universe) { //Recorremos todo el universo
                if (!p.test(x)) {
                    P = false;
                }
                if (q.test(x)) {
                    totalQ++;
                }
            }

            boolean existeQUnica = totalQ == 1;

            return P == existeQUnica;
        }

        static void tests() {
            // Exercici 1
            // Taules de veritat

            // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
            test(1, 1, 1, () -> exercici1(new char[]{IMPL, DISJ, DISJ}, new int[]{0, 2, 1, 0}) == 1);

            // Contradicció: (p0 . p0) ∧ p0
            test(1, 1, 2, () -> exercici1(new char[]{NAND, CONJ}, new int[]{0, 0, 0}) == 0);

            // Exercici 2
            // Equivalència
            test(1, 2, 1, () -> {
                return exercici2(new int[]{1, 2, 3}, (x) -> x == 0, (x) -> x == 0);
            });

            test(1, 2, 2, () -> {
                return exercici2(new int[]{1, 2, 3}, (x) -> x >= 1, (x) -> x % 2 == 0);
            });
        }
    }

    /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
     */
    static class Tema2 {

        /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
         */
        static int exercici1(int[] a) {

            int n = a.length; //Total elementos del conjunto

            int[][] stirling = new int[n + 1][n + 1]; //Creamos la tabla de números de Stirling

            stirling[0][0] = 1;

            //Rellenamos la tabla usando la fórmula recursiva: S(n, k) = k * S(n-1, k) + S(n-1, k-1)
            for (int i = 1; i <= n; i++) {

                for (int j = 1; j <= i; j++) {

                    stirling[i][j] = j * stirling[i - 1][j] + stirling[i - 1][j - 1];
                }
            }

            // Sumamos los S(n, k) para obtener el número total de particiones
            int particiones = 0;

            for (int k = 1; k <= n; k++) {
                particiones += stirling[n][k]; // Esta suma es el número de Bell B(n)
            }
            return particiones;
        }

        /*
     * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
     * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
     * antisimètrica.
     *
     * Si no existeix, retornau -1.
         */
        static int exercici2(int[] a, int[][] rel) {

            // Convertim rel a una llista de parells   
            List<List<Integer>> relació = new ArrayList<>();
            for (int[] parell : rel) {
                relació.add(Arrays.asList(parell[0], parell[1]));
            }

            // Clausura reflexiva: añadimos (x, x) para cada x ∈ a si no está presente
            for (int x : a) {
                List<Integer> reflexiu = Arrays.asList(x, x);
                if (!relació.contains(reflexiu)) {
                    relació.add(reflexiu);
                }
            }

            // Clausura transitiva: añadimos (x, z) si existen (x, y) y (y, z)
            boolean canviat;

            do {
                canviat = false;
                List<List<Integer>> afegir = new ArrayList<>();

                for (List<Integer> p1 : relació) {
                    for (List<Integer> p2 : relació) {
                        if (p1.get(1).equals(p2.get(0))) {
                            List<Integer> nou = Arrays.asList(p1.get(0), p2.get(1));
                            if (!relació.contains(nou) && !afegir.contains(nou)) {
                                afegir.add(nou);
                                canviat = true;
                            }
                        }
                    }
                }

                relació.addAll(afegir);

            } while (canviat);

            // Comprovació d'antisimetria: si (x, y) y (y, x) ∈ R y (x) diferente (y) entonces no es de orden parcial
            for (List<Integer> p : relació) {
                int x = p.get(0);
                int y = p.get(1);
                if (x != y && relació.contains(Arrays.asList(y, x))) {
                    return -1;
                }
            }

            // Si llegamos aquí, la clausura es válida y devolvemos su tamaño (número de pares)
            return relació.size();
        }

        /*
     * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
     * retornau:
     * - L'ínfim de `x` si existeix i `op` és false
     * - El suprem de `x` si existeix i `op` és true
     * - null en qualsevol altre cas
         */
        static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op) {

            // Construimos la relación como una matriz de accesibilidad
            boolean[][] mat = new boolean[a.length][a.length];

            // Llenamos la matriz de relación
            for (int[] par : rel) {
                int i = obtenerIndice(a, par[0]);
                int j = obtenerIndice(a, par[1]);
                mat[i][j] = true;
            }

            // Clausura reflexiva
            for (int i = 0; i < a.length; i++) {
                mat[i][i] = true;
            }

            // Clausura transitiva
            for (int k = 0; k < a.length; k++) {

                for (int i = 0; i < a.length; i++) {

                    for (int j = 0; j < a.length; j++) {

                        mat[i][j] = mat[i][j] || (mat[i][k] && mat[k][j]);
                    }
                }
            }

            // Inicializamos el candidato a ínfim/suprem como null
            Integer millor = null;

            for (int candidat : a) {

                boolean vàlid = true;

                for (int xi : x) {

                    int idxC = obtenerIndice(a, candidat);
                    int idxX = obtenerIndice(a, xi);

                    if (op) { // suprem: candidat >= Xi per tots

                        if (!mat[idxX][idxC]) {
                            vàlid = false;
                            break;
                        }

                    } else { // ínfim: candidat <= Xi per tots

                        if (!mat[idxC][idxX]) {
                            vàlid = false;
                            break;
                        }
                    }
                }

                if (vàlid) {  // Si el candidato cumple, actualizamos el "mejor" (mínimo supremo o máximo ínfimo)

                    if (millor == null) {
                        millor = candidat;

                    } else if (op) {

                        if (candidat < millor) {
                            millor = candidat; // En caso de suprem, elegimos el más pequeño entre los válidos
                        }

                    } else {

                        if (candidat > millor) {
                            millor = candidat; // En caso de ínfim, elegimos el más grande entre los válidos
                        }
                    }
                }
            }

            return millor;
        }

        private static int obtenerIndice(int[] a, int v) {
            
            for (int i = 0; i < a.length; i++) {
                if (a[i] == v) {
                    return i;
                }
            }
            return -1;
        }


        /*
     * Donada una funció `f` de `a` a `b`, retornau:
     *  - El graf de la seva inversa (si existeix)
     *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
     *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
     *  - Sinó, null.
         */
        
        static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f) {
            
            // Paso 1: Intentar inversa total (biyección)
            
            List<Integer> imagenes = new ArrayList<>();
            boolean inyectiva = true;
            
            for (int x : a) {
                
                int y = f.apply(x);
                
                if (imagenes.contains(y)) {
                    inyectiva = false;
                    break;
                }
                imagenes.add(y);
            }

            boolean exhaustiva = true;
            
            for (int y : b) {
                
                if (!imagenes.contains(y)) {
                    exhaustiva = false;
                    break;
                }
            }

            if (inyectiva && exhaustiva) {
                // Es inversa total
                List<int[]> inversa = new ArrayList<>();
                
                for (int y : b) {
                    
                    for (int x : a) {
                        
                        if (f.apply(x) == y) {
                            inversa.add(new int[]{y, x});
                            break;
                        }
                    }
                }
                return inversa.toArray(int[][]::new);
            }

            // Paso 2: Intentar inversa por la izquierda (usar solo elementos de a)
            List<int[]> inversaIzq = new ArrayList<>();
            
            for (int y : b) {
                
                boolean encontrado = false;
                
                for (int x : a) {
                    
                    if (f.apply(x) == y) {
                        inversaIzq.add(new int[]{y, x});
                        encontrado = true;
                        break;
                    }
                }
                
                if (!encontrado) {
                    inversaIzq = null;
                    break;
                }
            }
            if (inversaIzq != null) {
                return inversaIzq.toArray(int[][]::new);
            }

            /* Paso 3: Intentar inversa por la derecha (usar valores ficticios si hace falta)
             * Buscamos algún x (no necesariamente en a) tal que f(x) = y
             * Como no conocemos el dominio completo, probamos una cantidad razonable de x
             */
            
            List<int[]> inversaDer = new ArrayList<>();
            
            for (int y : b) {
                
                boolean encontrado = false;
                
                for (int x = -1000; x <= 1000; x++) {
                    
                    if (f.apply(x) == y) {
                        inversaDer.add(new int[]{y, x});
                        encontrado = true;
                        break;
                    }
                }
                
                if (!encontrado) {
                    
                    inversaDer = null;
                    break;
                }
            }
            
            if (inversaDer != null) {
                return inversaDer.toArray(int[][]::new);
            }

            // Si no hay ninguna forma posible
            return null;
        }

        /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
         */
        static void tests() {
            // Exercici 1
            // Nombre de particions

            test(2, 1, 1, () -> exercici1(new int[]{1}) == 1);
            test(2, 1, 2, () -> exercici1(new int[]{1, 2, 3}) == 5);

            // Exercici 2
            // Clausura d'ordre parcial
            final int[] INT02 = {0, 1, 2};

            test(2, 2, 1, () -> exercici2(INT02, new int[][]{{0, 1}, {1, 2}}) == 6);
            test(2, 2, 2, () -> exercici2(INT02, new int[][]{{0, 1}, {1, 0}, {1, 2}}) == -1);

            // Exercici 3
            // Ínfims i suprems
            final int[] INT15 = {1, 2, 3, 4, 5};
            final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
            final Integer ONE = 1;

            test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[]{2, 3}, false)));
            test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[]{2, 3}, true) == null);

            // Exercici 4
            // Inverses
            final int[] INT05 = {0, 1, 2, 3, 4, 5};

            test(2, 4, 1, () -> {
                var inv = exercici4(INT05, INT02, (x) -> x / 2);

                if (inv == null) {
                    return false;
                }

                inv = lexSorted(inv);

                if (inv.length != INT02.length) {
                    return false;
                }

                for (int i = 0; i < INT02.length; i++) {
                    if (inv[i][0] != i || inv[i][1] / 2 != i) {
                        return false;
                    }
                }

                return true;
            });

            test(2, 4, 2, () -> {
                var inv = exercici4(INT02, INT05, (x) -> x);

                if (inv == null) {
                    return false;
                }

                inv = lexSorted(inv);

                if (inv.length != INT05.length) {
                    return false;
                }

                for (int i = 0; i < INT02.length; i++) {
                    if (inv[i][0] != i || inv[i][1] != i) {
                        return false;
                    }
                }

                return true;
            });
        }

        /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
         */
        static int[][] lexSorted(int[][] arr) {
            
            if (arr == null) {
                return null;
            }

            var arr2 = Arrays.copyOf(arr, arr.length);
            Arrays.sort(arr2, Arrays::compare);
            return arr2;
        }

        /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
         */
        static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
            var rel = new ArrayList<int[]>();

            for (int a : as) {
                for (int b : bs) {
                    if (pred.test(a, b)) {
                        rel.add(new int[]{a, b});
                    }
                }
            }

            return rel.toArray(new int[][]{});
        }

        // Especialització de generateRel per as = bs
        static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
            return generateRel(as, as, pred);
        }
    }

    /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
     */
    static class Tema3 {

        /*
         * Determinau si el graf `g` (no dirigit) té cicles.
         */
        static boolean exercici1(int[][] g) {
            
            boolean[] visitat = new boolean[g.length];

            return comprobarCicles(g, 0, -1, visitat);
        }

        /*
     * Recorregut per detectar cicles en un graf no dirigit.
     * - g: graf representat com una llista d'adjacència
     * - u: node actual
     * - pare: node anterior en el camí (per evitar tornar enrere)
     * - visitat: marcatge de nodes visitats
         */
        
        static boolean comprobarCicles(int[][] g, int u, int pare, boolean[] visitat) {
            visitat[u] = true;

            for (int v : g[u]) {
                if (!visitat[v]) {
                    if (comprobarCicles(g, v, u, visitat)) {
                        return true;
                    }
                } else if (v != pare) {
                    return true; // Trobam una aresta cap enrere -> cicle
                }
            }

            return false;
        }

        /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
         */
        
        static boolean exercici2(int[][] g1, int[][] g2) {
            
            int n = g1.length;
            
            if (n != g2.length) {
                return false;   // Si el nombre de nodes és diferent, no poden ser isomorfs
            }
            int[] perm = IntStream.range(0, n).toArray();
            return permutacions(perm, 0, g1, g2);   // Provam totes les permutacions a veure si alguna fa que g1 ≅ g2
        }

        static boolean permutacions(int[] perm, int i, int[][] g1, int[][] g2) {
            
            if (i == perm.length) {  // Si ja hem fixat tota la permutació, comprovam si és un isomorfisme
                return esIsomorf(g1, g2, perm);
            }

            // Provam totes les permutacions a partir de la posició i
            for (int j = i; j < perm.length; j++) {
                
                cambiar(perm, i, j);
                
                if (permutacions(perm, i + 1, g1, g2)) {
                    return true;
                }
                
                cambiar(perm, i, j);
            }

            return false;
        }

        static boolean esIsomorf(int[][] g1, int[][] g2, int[] perm) {
            
            int n = g1.length;
            for (int i = 0; i < n; i++) {
                
                List<Integer> adjG1 = Arrays.stream(g1[i]).map(j -> perm[j]).sorted().boxed().toList(); // Reindexam les arestes de g1 segons la permutació y aplicam la permutació
                List<Integer> adjG2 = Arrays.stream(g2[perm[i]]).sorted().boxed().toList();    // Arestes del node permutat a g2
                
                if (!adjG1.equals(adjG2)) {
                    return false; // Si la llista d'adjacència no coincideix, no és isomorfisme
                }
            }
            return true;
        }

        static void cambiar(int[] a, int i, int j) {
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }

        /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
         */
        
        static int[] exercici3(int[][] g, int r) {
            
            // Un grafo es un árbol si es conexo y no tiene ciclos
            if (!esArbol(g)) {
                return null;
            }

            // Realizar recorrido en postorden
            List<Integer> postorden = new ArrayList<>();
            boolean[] visitado = new boolean[g.length];
            
            postorden(g, r, -1, visitado, postorden);

            // Convertir la lista a array
            int[] resultado = new int[postorden.size()];
            
            for (int i = 0; i < resultado.length; i++) {
                
                resultado[i] = postorden.get(i);
            }

            return resultado;
        }

        // Método auxiliar para verificar si es un árbol
        private static boolean esArbol(int[][] g) {
            
            if (g.length == 0) {
                return true; // árbol vacío
            }
            
            boolean[] visitado = new boolean[g.length];
            
            if (tieneCiclos(g, 0, -1, visitado)) {
                return false;
            }

            // Verificar si es conexo
            for (boolean v : visitado) {
                
                if (!v) {
                    return false;
                }
            }

            return true;
        }

        // Método auxiliar para detectar ciclos
        private static boolean tieneCiclos(int[][] g, int u, int padre, boolean[] visitado) {
            visitado[u] = true;

            for (int v : g[u]) {
                
                if (!visitado[v]) {
                    if (tieneCiclos(g, v, u, visitado)) {
                        return true;
                    }
                } else if (v != padre) {
                    return true; // //Si se detecta un ciclo se devuelve true
                }
            }

            return false;
        }

        // Método auxiliar para recorrido postorden
        private static void postorden(int[][] g, int u, int padre, boolean[] visitado, List<Integer> resultado) {
            
            for (int v : g[u]) {
                if (v != padre) {
                    postorden(g, v, u, visitado, resultado);
                }
            }
            resultado.add(u);
        }

        /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
         */
 /* 
         * Se obtienen las posiciones en el mapa correspondientes al 
         * nodo origen ('O') y al nodo destino ('D') y se busca la distancia 
         * mínima del recorrido entre O y D. En caso de no haber un recorrido 
         * mínimo (No existe recorrido entre ambos nodos) se devuelve 
         * el valor -1.
         */
        
        static int exercici4(char[][] mapa) {

            //Dimensiones n y m del mapa
            int filas = mapa.length;
            int columnas = mapa[0].length;

            //Validación de las dimensiones del mapa 
            if (filas == 0 || columnas == 0) {
                return -1;
            }

            //Valores de las posiciones del nodo Origen y destino
            int[] nodoOrigen = identificarCaracter(mapa, 'O');
            int[] nodoDestino = identificarCaracter(mapa, 'D');

            //Validación de valores/posiciones invalidas
            if (nodoOrigen[0] < 0 || nodoDestino[0] < 0) {
                return -1;
            }

            //Valor de la distancia mínima entre el recorrido de ambos nodos O y D
            return distanciaMinima(mapa, nodoOrigen, nodoDestino);
        }

        //Identificar la fila y columna sobre la que se ubica un "caracter" (nodo) en el mapa
        private static int[] identificarCaracter(char[][] mapa, char caracter) {
            int[] posicion = {-1, -1};

            //Bucle que recorre el mapa
            for (int i = 0; i < mapa.length; i++) {
                for (int j = 0; j < mapa[0].length; j++) {
                    //Comrpobación del caracter
                    if (mapa[i][j] == caracter) {
                        posicion[0] = i;
                        posicion[1] = j;
                    }
                }
            }

            //Array con la posición x e y del nodo 
            return posicion;
        }

        /* 
         * Se recorre el grafo por niveles de distancia respecto del nodo origen 
         * para calcular su distancia mínima. Se empieza desde el nodo origen 
         * iterando. Se comprueban sus adayacentes, todos aquellos válidos se 
         * marcan como visitados, se encolan y se incrementa en uno la distancia
         * (Sabemos por tanto que por lo menos existe un camino de distancia 1 
         * respecto del origen) Este proceso se repite hasta encontrar D 
         * (devolviendo la distancia mínima) o hasta que ya no haya más 
         * posiciones por vistar (devolviendo -1).
         */
        private static int distanciaMinima(char[][] mapa, int[] nodoOrigen, int[] nodoDestino) {

            int filas = mapa.length;
            int columnas = mapa[0].length;
            int dimensionMapa = filas * columnas;

            int[] coordenadaX = new int[dimensionMapa];
            int[] coordenadaY = new int[dimensionMapa];
            int[] distanciaOrigen = new int[dimensionMapa];
            boolean[][] casillasVisitadas = new boolean[filas][columnas];

            int cabezaCola = 0; //Apunta a la siguiente celda a procesar
            int punteroCola = 0; //Apunta a la siguiente celda libre en la cola

            encolar(coordenadaX, coordenadaY, distanciaOrigen, nodoOrigen[0], nodoOrigen[1], 0, punteroCola++);
            casillasVisitadas[nodoOrigen[0]][nodoOrigen[1]] = true;

            int[] direccionesX = {-1, 1, 0, 0};
            int[] direccionesY = {0, 0, -1, 1};

            while (cabezaCola < punteroCola) {

                int x = coordenadaX[cabezaCola];
                int y = coordenadaY[cabezaCola];
                int d = distanciaOrigen[cabezaCola++];

                for (int k = 0; k < 4; k++) {

                    int nodoVecinoX = x + direccionesX[k];
                    int nodoVecinoY = y + direccionesY[k];

                    if (casillaValida(mapa, nodoVecinoX, nodoVecinoY, casillasVisitadas)) {

                        casillasVisitadas[nodoVecinoX][nodoVecinoY] = true;

                        int distanciaNodo = d + 1;

                        if (nodoVecinoX == nodoDestino[0] && nodoVecinoY == nodoDestino[1]) {
                            return distanciaNodo;
                        }

                        encolar(coordenadaX, coordenadaY, distanciaOrigen, nodoVecinoX, nodoVecinoY, distanciaNodo, punteroCola++);
                    }
                }
            }

            return -1;
        }

        /* 
         *   "Antes de visitar a un vecino comprueba su validez." 
         *   Comprueba que la casilla a la que se apunta cumpla 
         *   los siguientes parámetros:
         *   La casilla este dentro de los límites del tablero, 
         *   la casilla no contenga un obstáculo (Sea una casilla 
         *   válida a visitar) y no sea una casilla que ya 
         *   haya sido visitada. Por tanto devuelve verdadero si 
         *   la casilla está dentro de rango, no contiene un obstáculo 
         *   y no ha sido préviamente visitada)
         */
        //Método que valida la casilla adyacente respecto del nodo o casilla sobre la que se itera
        private static boolean casillaValida(char[][] mapa, int x, int y, boolean[][] casillasVisitadas) {
            //Comprueba si la casilla está dentro de los límites del mapa
            boolean fueraDeRango = x < 0 || x >= mapa.length || y < 0 || y >= mapa[0].length;

            if (fueraDeRango) {
                return false;
            }

            //Comprueba si la casilla contiene un obstáculo
            boolean esObstaculo = mapa[x][y] == '#';

            if (esObstaculo) {
                return false;
            }

            //Comprueba si la casilla ha sido visitada
            boolean casillaVisitada = casillasVisitadas[x][y];

            if (casillaVisitada) {
                return false;
            }

            //Deuvelve verdadero si cumple con lo anterior 
            return true;
        }

        /*
         * Método que coloca al final de la cola los valores de cada componente
         * del vecino visitado (posición respecto a X e Y y la distancia que 
         * hay desde el origen).
         */
        private static void encolar(int[] coordenadaX, int[] coordenadaY, int[] distanciaOrigen, int x, int y, int distancia, int indice) {
            coordenadaX[indice] = x;
            coordenadaY[indice] = y;
            distanciaOrigen[indice] = distancia;
        }

        /*
         * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
         */
        static void tests() {

            final int[][] D2 = {{}, {}};
            final int[][] C3 = {{1, 2}, {0, 2}, {0, 1}};

            final int[][] T1 = {{1, 2}, {0}, {0}};
            final int[][] T2 = {{1}, {0, 2}, {1}};

            // Exercici 1
            // G té cicles?
            test(3, 1, 1, () -> !exercici1(D2));
            test(3, 1, 2, () -> exercici1(C3));

            // Exercici 2
            // Isomorfisme de grafs
            test(3, 2, 1, () -> exercici2(T1, T2));
            test(3, 2, 1, () -> !exercici2(T1, C3));

            // Exercici 3
            // Postordre
            test(3, 3, 1, () -> exercici3(C3, 1) == null);
            test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[]{1, 2, 0}));

            // Exercici 4
            // Laberint
            test(3, 4, 1, () -> {
                return -1 == exercici4(new char[][]{
                    " #O".toCharArray(),
                    "D# ".toCharArray(),
                    " # ".toCharArray(),});
            });

            test(3, 4, 2, () -> {
                return 8 == exercici4(new char[][]{
                    "###D".toCharArray(),
                    "O # ".toCharArray(),
                    " ## ".toCharArray(),
                    "    ".toCharArray(),});
            });
        }
    }

    /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
     */
    static class Tema4 {

        /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
         */
        static int[] exercici1(String msg, int n, int e) {

            byte[] bytes = msg.getBytes(); //Transformar el mensaje en bytes
            int numeroBloques = bytes.length / 2; //Agrupar los bytes de 2 en 2 (bloques a cifrar)
            int[] bloquesCifrados = new int[numeroBloques];

            //Codificación de dos caracteres (2 bytes) en un único numero 
            for (int i = 0; i < numeroBloques; i++) {

                int byte1 = bytes[2 * i] & 0xFF;
                int byte2 = bytes[2 * i + 1] & 0xFF;
                int m = byte1 * 128 + byte2;

                //Codificación de la pareja de bytes
                bloquesCifrados[i] = cifradoRSA(m, e, n);
            }

            return bloquesCifrados;
        }

        /*
            Se inicia reduciendo la base del número a codificar. Se descompone 
            el exponente en bits y se recorre su cadena desde el menos 
            significativo al más significativo. En caso de que su bit menos 
            significativo valga 1 -> resultado * x si no se eleva x (base) y se 
            reduce con módulo mod.
            
         */
        private static int cifradoRSA(int base, int exponente, int mod) {

            int resultado = 1;
            int x = base % mod; //Base reducida módulo mod

            while (exponente > 0) {
                
                if ((exponente & 1) == 1) {
                    resultado = (int) ((long) resultado * x % mod);
                }
                
                x = (int) ((long) x * x % mod);
                
                exponente /= 2; //Desplazamiento del exponente hacia la derecha
            }

            return resultado;

        }


        /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
         */
        static String exercici2(int[] m, int n, int e) {

            int p = 0, q = 0;
            boolean encontrado = false;

            //Bucle que busca dos factores primos, p y q, de n
            for (int i = 2; i * i <= n && !encontrado; i++) {
                
                if (n % i == 0) {
                    p = i;
                    q = n / i;
                    encontrado = true;
                }
            }

            int phi = (p - 1) * (q - 1); //Cálculo de la phi de Euler para el posterior cálculo del inverso modular

            int d = obtenerInversoModular(e, phi); //Clave privada

            byte[] bloqueBytes = new byte[m.length * 2];

            for (int i = 0; i < m.length; i++) {
                
                int valorDescifrado = elevarModulo(m[i], d, n); //Cifrado inverso para cada número del array m

                //Empaquetar el valor númerico de un bloque RSA en forma dos bytes
                int b1 = valorDescifrado / 128;
                int b2 = valorDescifrado % 128;

                bloqueBytes[2 * i] = (byte) b1;
                bloqueBytes[2 * i + 1] = (byte) b2;
            }

            //Se obtiene el mensaje original del conjunto de bytes desempaquetados
            return new String(bloqueBytes);
        }

        private static int elevarModulo(int base, int exponente, int mod) {

            int resultado = 1;
            int x = base % mod; //Base reducida módulo mod

            while (exponente > 0) {
                if ((exponente & 1) == 1) {
                    resultado = (int) ((long) resultado * x % mod);
                }
                x = (int) ((long) x * x % mod);
                exponente /= 2; //Desplazamiento del exponente hacia la derecha
            }

            return resultado;
        }

        //Método que calcula el inverso de a módulo m
        private static int obtenerInversoModular(int a, int m) {

            //Se utiliza el algoritmo de Euclides para obtener el inverso X y su mcd(a,b)
            int[] eg = euclidesExtendido(a, m);

            int mcd = eg[0];

            int x = eg[1]; //Inversa de a, [a]^-1 n

            //Si mcd(a,b) != 1, no invertibel y por tanto no existe un [x]n tal que [a]n*[x]n = [1]n
            if (mcd != 1) {
                throw new ArithmeticException("No existe un inverso modular para " + a + " mod " + m + ".");
            }

            //Ajustamos x, con x mod n, para obtener su representante canónico ([X]n ∈ 0 <= X < m)
            x %= m;

            //En caso de que x no sea positivo se ajusta x con la suma del módulo
            if (x < 0) {

                x += m;
            }

            return x;
        }

        private static int[] euclidesExtendido(int a, int b) {

            //Módulo == 0
            if (b == 0) {
                return new int[]{a, 1, 0};

            } else {

                //Sabiendo que el mcd(a,b) = mcd(b,r) se aplica el algoritmo de manera recursiva hasta obtener el mcd(a,b)
                int[] rec = euclidesExtendido(b, a % b);

                int mcd = rec[0], x1 = rec[1], y1 = rec[2];

                //Se actualizan los parámetros x e y hasta obtener los valores de Xi e Yi en la última interación antes de r == 0 
                int x = y1;

                int y = x1 - (a / b) * y1;

                return new int[]{mcd, x, y}; //a*x + b*y = mcd(a,b)

            }
        }

        static void tests() {
            // Exercici 1
            // Codificar i encriptar
            test(4, 1, 1, () -> {
                var n = 2 * 8209;
                var e = 5;

                var encr = exercici1("Patata", n, e);
                return Arrays.equals(encr, new int[]{4907, 4785, 4785});
            });

            // Exercici 2
            // Desencriptar i decodificar
            test(4, 2, 1, () -> {
                var n = 2 * 8209;
                var e = 5;

                var encr = new int[]{4907, 4785, 4785};
                var decr = exercici2(encr, n, e);
                return "Patata".equals(decr);
            });
        }
    }

    /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
     */
    public static void main(String[] args) {
        System.out.println("---- Tema 1 ----");
        Tema1.tests();
        System.out.println("---- Tema 2 ----");
        Tema2.tests();
        System.out.println("---- Tema 3 ----");
        Tema3.tests();
        System.out.println("---- Tema 4 ----");
        Tema4.tests();
    }

    // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
    static void test(int tema, int exercici, int test, BooleanSupplier p) {
        try {
            if (p.getAsBoolean()) {
                System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
            } else {
                System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
            }
        } catch (Exception e) {
            if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
                System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
            } else {
                System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
                e.printStackTrace();
            }
        }
    }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :
