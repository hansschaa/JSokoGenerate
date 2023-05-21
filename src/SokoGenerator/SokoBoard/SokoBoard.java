/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator.SokoBoard;

import SokoGenerator.Coordenate;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Hans
 */
public class SokoBoard {
    public static int width;
    public static int height;
    public static TemplateFactory templateFactory;
    public static Random random = new Random();
    public static FloodFill floodFill = new FloodFill();
    public static char WhiteSpace = '0';
    public static char StandarSpace = '9';
    public static int levelCount = 1;
    public static ArrayList<char[][]> templatesCandidates = new ArrayList();
    public static Hashtable<Integer, ArrayList<char[][]>> templatesDict;
    public ArrayList<char[][]> levels = new ArrayList();

    public SokoBoard() throws FileNotFoundException {
        templatesDict = new Hashtable();
        templateFactory = new TemplateFactory();
    }

    public void GenerateLevels() throws FileNotFoundException {
        System.out.println("GenerateLevels");
        this.levels.clear();

        for(int i = 0; i < levelCount; ++i) {
            SetupDimension();
            char[][] levelGenerated = null;

            do {
                try {
                    levelGenerated = GenerateLevel();
                } catch (Exception var4) {
                    System.out.println(var4);
                    var4.printStackTrace();
                    System.out.println();
                }
            } while(levelGenerated == null);

            this.levels.add(levelGenerated);
        }

        this.PrintLevels();
    }

    private static char[][] GenerateLevel() {
        System.out.println("--> GenerateLevel...");
        char[][] level = new char[height][width];
        FillWEmpty(level);

        for(int i = 0; i + 5 <= height; i += 3) {
            for(int j = 0; j + 5 <= width; j += 3) {
                Iterator var3 = templateFactory.templates.iterator();

                while(var3.hasNext()) {
                    Template template = (Template)var3.next();
                    char[][] bounds = new char[4][5];
                    bounds[0] = GetBound(level, i, j, Dir.TOP);
                    bounds[1] = GetBound(level, i, j, Dir.RIGHT);
                    bounds[2] = GetBound(level, i, j, Dir.BOTTOM);
                    bounds[3] = GetBound(level, i, j, Dir.LEFT);

                    for(int r = 0; r < 4; ++r) {
                        Template templateTemp = template.Clone();
                        templateTemp.Rotate(r);
                        if (!ContainsTemplate(templateFactory.templates.indexOf(template), templateTemp.template) && TryPut(templateTemp.template, bounds)) {
                            AddTemplate(templateFactory.templates.indexOf(template), templateTemp.template);
                        }

                        templateTemp.FlipX();
                        if (!ContainsTemplate(templateFactory.templates.indexOf(template), templateTemp.template) && TryPut(templateTemp.template, bounds)) {
                            AddTemplate(templateFactory.templates.indexOf(template), templateTemp.template);
                        }

                        templateTemp.FlipX();
                        templateTemp.FlipY();
                        if (!ContainsTemplate(templateFactory.templates.indexOf(template), templateTemp.template) && TryPut(templateTemp.template, bounds)) {
                            AddTemplate(templateFactory.templates.indexOf(template), templateTemp.template);
                        }
                    }
                }

                if (templatesCandidates.size() == 0) {
                    return null;
                }

                int randomTemplateIndex = random.nextInt(templatesCandidates.size());
                char[][] chosenCandidate = (char[][])templatesCandidates.get(randomTemplateIndex);
                PutCandidate(chosenCandidate, level, i, j);
                templatesCandidates.clear();
                templatesDict.clear();
            }
        }

        RepairBounds(level);
        if (HasHugeArea(level)) {
            return null;
        } else if (IsAllConnected(level)) {
            return level;
        } else {
            return null;
        }
    }

    public static void AddTemplate(int templateID, char[][] newTemplate) {
        if (!templatesDict.containsKey(templateID)) {
            ArrayList<char[][]> aux = new ArrayList();
            templatesDict.put(templateID, aux);
        }

        char[][] clone = (char[][])Arrays.stream(newTemplate).map((rec$) -> {
            return (char[])((char[])rec$).clone();
        }).toArray((x$0) -> {
            return new char[x$0][];
        });
        ((ArrayList)templatesDict.get(templateID)).add(clone);
        templatesCandidates.add(clone);
    }

    private static boolean EqualsTemplate(char[][] template, char[][] otherTemplate) {
        int total = 0;
        int max = 25;

        for(int i = 0; i < template.length; ++i) {
            for(int j = 0; j < template[0].length; ++j) {
                if (template[i][j] == otherTemplate[i][j]) {
                    ++total;
                }
            }
        }

        if (total == max) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean ContainsTemplate(int newTemplateID, char[][] newTemplate) {
        if (templatesDict.containsKey(newTemplateID)) {
            ArrayList<char[][]> templates = (ArrayList)templatesDict.get(newTemplateID);
            Iterator var3 = templates.iterator();

            while(var3.hasNext()) {
                char[][] template = (char[][])var3.next();
                if (EqualsTemplate(newTemplate, template)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean HasHugeArea(char[][] level) {
        int levelH = level.length;
        int levelW = level[0].length;
        boolean result = false;

        for(int i = 0; i < levelH - 4; ++i) {
            for(int j = 0; j < levelW - 4; ++j) {
                if (level[i][j] == ' ') {
                    result = CheckHugeAreaTile(level, i, j);
                    if (result) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean CheckHugeAreaTile(char[][] level, int i, int j) {
        int iMax = i + 3;
        int jMax = j + 3;

        for(int ii = i; ii < iMax; ++ii) {
            for(int jj = j; jj < jMax; ++jj) {
                if (level[ii][jj] != ' ') {
                    return false;
                }
            }
        }

        if (level[iMax][jMax - 1] == ' ') {
            return true;
        } else if (level[iMax - 1][jMax] == ' ') {
            return true;
        } else {
            return false;
        }
    }

    private static void RepairBounds(char[][] level) {
        int i;
        for(i = 0; i < width; ++i) {
            level[0][i] = '#';
        }

        for(i = 0; i < width; ++i) {
            level[height - 1][i] = '#';
        }

        for(i = 0; i < height; ++i) {
            level[i][0] = '#';
        }

        for(i = 0; i < height; ++i) {
            level[i][width - 1] = '#';
        }

    }

    private static boolean IsAllConnected(char[][] level) {
        /*System.out.println("IsAllConnected");
        ArrayList<Coordenate> result = floodFill.TryConnected(level);
        int maxWhiteSpaces = floodFill.GetWhiteSpaces(level);
        return maxWhiteSpaces == result.size();*/
        
        //HACK
        return true;
    }

    private static boolean TryPut(char[][] template, char[][] bounds) {
        int lenght = template.length;

        int i;
        for(i = 0; i < lenght; ++i) {
            if (template[0][i] == ' ' && bounds[0][i] != ' ') {
                return false;
            }
        }

        for(i = 0; i < lenght; ++i) {
            if (template[lenght - 1][i] == ' ' && bounds[2][i] != ' ') {
                return false;
            }
        }

        for(i = 0; i < lenght; ++i) {
            if (template[i][0] == ' ' && bounds[3][i] != ' ') {
                return false;
            }
        }

        for(i = 0; i < lenght; ++i) {
            if (template[i][lenght - 1] == ' ' && bounds[1][i] != ' ') {
                return false;
            }
        }

        return true;
    }

    private static void PutCandidate(char[][] chosenCandidate, char[][] level, int i, int j) {
        int maxI = i + chosenCandidate.length;
        int maxJ = j + chosenCandidate.length;
        int contI = 0;
        int contJ = 0;

        for(int ii = i; ii < maxI; ++ii) {
            for(int jj = j; jj < maxJ; ++jj) {
                if ((level[ii][jj] != ' ' || chosenCandidate[contI][contJ] != '0') && (level[ii][jj] != '#' || chosenCandidate[contI][contJ] != '0')) {
                    level[ii][jj] = chosenCandidate[contI][contJ];
                    ++contJ;
                } else {
                    ++contJ;
                }
            }

            contJ = 0;
            ++contI;
        }

    }

    private static char[] GetBound(char[][] level, int i, int j, Dir dir) {
        char[] bound = new char[5];
        int maxJ = j + 5;
        int maxI = i + 5;
        int cont = 0;
        int ii;
        switch (dir) {
            case TOP:
                if (i == 0) {
                    return new char[]{'9', '9', '9', '9', '9'};
                }

                for(ii = j; ii < maxJ; ++ii) {
                    bound[cont] = level[i - 1][ii];
                    ++cont;
                }

                return bound;
            case BOTTOM:
                if (i == height - 5) {
                    return new char[]{'9', '9', '9', '9', '9'};
                }

                for(ii = j; ii < maxJ; ++ii) {
                    bound[cont] = level[i + 1][ii];
                    ++cont;
                }

                return bound;
            case LEFT:
                if (j == 0) {
                    return new char[]{'9', '9', '9', '9', '9'};
                }

                for(ii = i; ii < maxI; ++ii) {
                    bound[cont] = level[ii][j - 1];
                    ++cont;
                }

                return bound;
            case RIGHT:
                if (j == level[0].length - 5) {
                    return new char[]{'9', '9', '9', '9', '9'};
                }

                for(ii = i; ii < maxI; ++ii) {
                    bound[cont] = level[ii][j + 1];
                    ++cont;
                }
        }

        return bound;
    }

    private static void ShowLevel(char[][] level) {
        for(int i = 0; i < level.length; ++i) {
            for(int j = 0; j < level[0].length; ++j) {
                System.out.print(level[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    private static void FillWEmpty(char[][] level) {
        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                level[i][j] = StandarSpace;
            }
        }

    }

    public void PrintLevels() {
        Iterator var1 = this.levels.iterator();

        while(var1.hasNext()) {
            char[][] level = (char[][])var1.next();
            ShowLevel(level);
        }

    }

    private static void SetupDimension() {
        new Random();
        width = 2;
        height = 2;
        width *= 3;
        height *= 3;
        width += 2;
        height += 2;
    }

}
