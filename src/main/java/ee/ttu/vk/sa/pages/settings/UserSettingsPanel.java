package ee.ttu.vk.sa.pages.settings;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import ee.ttu.vk.sa.CustomAuthenticatedWebSession;
import ee.ttu.vk.sa.domain.Teacher;
import ee.ttu.vk.sa.service.TeacherService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by vadimstrukov on 2/27/16.
 */
public  class UserSettingsPanel extends Panel {

    @SpringBean
    private TeacherService teacherService;
    private BootstrapForm<Teacher> form;

    public UserSettingsPanel(String id) {
        super(id);
        Teacher authTeacher = CustomAuthenticatedWebSession.getSession().getTeacher();
        form = new BootstrapForm<>("form", new CompoundPropertyModel<>(authTeacher));
        form.add(new RequiredTextField<>("name", Model.of(authTeacher.getName())));
        form.add(new EmailTextField("email", Model.of(authTeacher.getEmail())).setEnabled(false));
        form.add(new PasswordTextField("password"));
        form.add(new AjaxSubmitLink("save", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> ajaxForm) {
                teacherService.saveTeacher(authTeacher);
            }
        });
        add(form);
    }

}
