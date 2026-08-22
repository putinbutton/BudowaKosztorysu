package kamilzadroga.BudowaKosztorysu.model;

import kamilzadroga.BudowaKosztorysu.service.CurrentUserService;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomRevisionListener implements RevisionListener {


    private static CurrentUserService currentUserServiceStatic;

    @Autowired
    public void setCurrentUserService(CurrentUserService currentUserService) {
        CustomRevisionListener.currentUserServiceStatic = currentUserService;
    }

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;

        try {
            customRevisionEntity.setUser(currentUserServiceStatic.getCurrentUser());
        } catch (Exception e) {
            customRevisionEntity.setUser(null);
        }

    }
}
