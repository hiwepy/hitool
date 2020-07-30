package hitool.freemarker.template;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

/**
 * A template.
 * <p/>
 * A template is used as a model for rendering output.
 * This object contains basic common template information
 */
public class Template implements Cloneable {
    String dir;
    String theme;
    String name;

    /**
     * Constructor.
     *
     * @param dir  base folder where the template is stored.
     * @param theme  the theme of the template
     * @param name   the name of the template.
     */
    public Template(String dir, String theme, String name) {
        this.dir = dir;
        this.theme = theme;
        this.name = name;
    }

    public String getDir() {
        return dir;
    }

    public String getTheme() {
        return theme;
    }

    public String getName() {
        return name;
    }

    public List<Template> getPossibleTemplates(ServletContext servletContext,TemplateEngine engine) {
        List<Template> list = new ArrayList<Template>(3);
        Template template = this;
        String parentTheme;
        list.add(template);
        while ((parentTheme = (String) engine.getThemeProps(servletContext,template).get("parent")) != null) {
            try {
                template = (Template) template.clone();
                template.theme = parentTheme;
                list.add(template);
            } catch (CloneNotSupportedException e) {
                // do nothing
            }
        }

        return list;
    }

    /**
     * Constructs a string in the format <code>/dir/theme/name</code>.
     * @return a string in the format <code>/dir/theme/name</code>.
     */
    public String toString() {
        return "/" + dir + "/" + theme + "/" + name;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (dir != null ? !dir.equals(template.dir) : template.dir != null) return false;
        if (name != null ? !name.equals(template.name) : template.name != null) return false;
        if (theme != null ? !theme.equals(template.theme) : template.theme != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dir != null ? dir.hashCode() : 0;
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
