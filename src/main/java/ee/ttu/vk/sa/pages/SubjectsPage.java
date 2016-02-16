package ee.ttu.vk.sa.pages;


import ee.ttu.vk.sa.domain.Subject;
import ee.ttu.vk.sa.pages.panels.FileUploadPanel;
import ee.ttu.vk.sa.pages.panels.IAction;
import ee.ttu.vk.sa.service.SubjectService;
import ee.ttu.vk.sa.utils.XlsParser;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

@AuthorizeInstantiation(Roles.ADMIN)
public class SubjectsPage extends AbstractPage implements IAction<Subject> {

	@SpringBean
	private SubjectService subjectService;

	private FileUploadPanel<Subject> panel;
    @SpringBean
    private SubjectService subjectService;
    private DataView<Subject> subjects;
    private SubjectDataProvider subjectDataProvider;
    private WebMarkupContainer subjectTable;
    private SubjectsPanel subjectPanel;
    private FileUploadPanel<Subject> panel;

	public SubjectsPage() {
		panel = new FileUploadPanel<>("panel", "xls", this);
		add(panel);
	}
    public SubjectsPage(){
        subjectDataProvider = new SubjectDataProvider();
        subjectTable = new WebMarkupContainer("subjectTable");
        subjectTable.setOutputMarkupId(true);
        subjectPanel = new SubjectsPanel("subjectPanel", new CompoundPropertyModel<>(new Subject()));
        subjects = getSubjects();
        subjects.setItemsPerPage(10);
        subjectTable.add(subjects);
        panel = new FileUploadPanel<>("xlsPanel", new XlsParser(), this);
        add(subjectTable, panel, new BootstrapAjaxPagingNavigator("navigator", subjects), subjectPanel, getButtonAddSubject(), getSearchForm());
    }

    private DataView<Subject> getSubjects(){
        return new DataView<Subject>("subjects", subjectDataProvider) {
            @Override
            protected void populateItem(Item<Subject> item) {
                item.add(new AjaxLink<Subject>("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        subjectPanel.setModel(item.getModel());
                        ajaxRequestTarget.add(subjectPanel);
                        subjectPanel.appendShowDialogJavaScript(ajaxRequestTarget);
                    }
                }.add(new Label("code")));
                item.add(new Label("name"));
                item.add(new Label("teacher.name"));
                item.add(new AjaxLink<Teacher>("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        subjectService.deleteSubject(item.getModelObject());
                        ajaxRequestTarget.add(subjectTable);
                    }

                    @Override
                    public boolean isVisible() {
                        return CustomAuthenticatedWebSession.getSession().isSignedIn();
                    }
                });
            }
        };
    }

    private BootstrapForm<Subject> getSearchForm(){
        BootstrapForm<Subject> form = new BootstrapForm<>("searchForm", new CompoundPropertyModel<>(new Subject()));
        form.add(new TextField<String>("code").add(new AjaxFormComponentUpdatingBehavior("input") {
            @Override
            protected void onUpdate(AjaxRequestTarget ajaxRequestTarget) {
                subjectDataProvider.setFilterState(form.getModelObject());
                ajaxRequestTarget.add(subjectTable);
            }
        }));
        return form;
    }

    private AjaxLink<Student> getButtonAddSubject(){
        return new AjaxLink<Student>("addSubject") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                subjectPanel.setModel(new CompoundPropertyModel<>(new Subject()));
                ajaxRequestTarget.add(subjectPanel);
                subjectPanel.appendShowDialogJavaScript(ajaxRequestTarget);
            }
            @Override
            public boolean isEnabled() {
                return CustomAuthenticatedWebSession.getSession().isSignedIn();
            }
        };
    }

	@Override
	public void save(InputStream objects) {
		List<Subject> subjects = subjectService.parseSubjects(objects);
		subjectService.saveSubjects(subjects);
	}
}
