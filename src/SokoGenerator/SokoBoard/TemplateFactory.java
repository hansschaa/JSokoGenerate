//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator.SokoBoard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TemplateFactory {
    public ArrayList<Template> templates;
    public int templateDim = 5;

    public TemplateFactory() throws FileNotFoundException {
        this.LoadTemplates();
    }

    public void LoadTemplates() throws FileNotFoundException {
        this.templates = new ArrayList();
        File file = new File(System.getProperty("user.dir") + "\\templates.txt");
        Scanner sc = new Scanner(file);
        char[][] template = new char[this.templateDim][this.templateDim];
        int cont = 0;

        while(true) {
            String line;
            do {
                if (!sc.hasNextLine()) {
                    return;
                }

                line = sc.nextLine();
            } while(line.length() == 0);

            for(int j = 0; j < line.length(); ++j) {
                template[cont][j] = line.charAt(j);
            }

            ++cont;
            if (cont == this.templateDim) {
                cont = 0;
                this.templates.add(new Template(template));
                template = new char[this.templateDim][this.templateDim];
            }
        }
    }
}
