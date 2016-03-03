package ee.ttu.vk.sa.pages.settings;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import ee.ttu.vk.sa.CustomAuthenticatedWebSession;
import ee.ttu.vk.sa.domain.Attendance;
import ee.ttu.vk.sa.domain.Teacher;
import ee.ttu.vk.sa.service.TeacherService;
import ee.ttu.vk.sa.utils.PasswordEncryptor;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

/**
 * Created by vadimstrukov on 2/27/16.
 */
public  class UserSettingsPanel extends Panel {

    @SpringBean
    private TeacherService teacherService;

    private BootstrapForm<Teacher> form;
    private NotificationPanel notificationPanel;


    public UserSettingsPanel(String id) {
        super(id);
        FormComponent password, cpassword;
        Teacher authTeacher = CustomAuthenticatedWebSession.getSession().getTeacher();
        notificationPanel = new NotificationPanel("feedback");
        notificationPanel.setOutputMarkupId(true);
        form = new BootstrapForm<>("form", new CompoundPropertyModel<>(authTeacher));
        form.add(new RequiredTextField<>("name").setRequired(true));
        form.add(new EmailTextField("email").setRequired(true));
        form.add(password = getPasswordTextField("password", null, "settings.fields.password"));
        form.add(cpassword = getPasswordTextField("cpassword", new PropertyModel<>(form.getModelObject(), "password"), "settings.fields.cpassword"));
        form.add(new EqualPasswordInputValidator(password, cpassword));
        form.add(new AjaxSubmitLink("save", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> ajaxForm) {
                teacherService.save((Teacher) ajaxForm.getModelObject());
                target.add(notificationPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(notificationPanel);
            }
        });
        add(notificationPanel);
        add(form);
    }

    private FormComponent<String> getPasswordTextField(String id, IModel<String> model, String label) {
        return new PasswordTextField(id, model).setLabel(new ResourceModel(label)).setRequired(true);
    }
}