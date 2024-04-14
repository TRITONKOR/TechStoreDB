package com.tritonkor.domain.services.handler;

import com.tritonkor.domain.services.handler.ClientHandler.PasswordHandler;
import com.tritonkor.domain.services.handler.ClientHandler.UsernameHandler;
import com.tritonkor.domain.services.handler.ReviewHandler.CreateTimeHandler;
import com.tritonkor.domain.services.handler.ReviewHandler.GradeHandler;
import com.tritonkor.domain.services.handler.ReviewHandler.OwnerIdHandler;
import com.tritonkor.domain.services.handler.ReviewHandler.TechnqiueIdHandler;
import com.tritonkor.domain.services.handler.ReviewHandler.TextHandler;
import com.tritonkor.domain.services.handler.TechniqueHandler.CompanyHandler;
import com.tritonkor.domain.services.handler.TechniqueHandler.ModelHandler;
import com.tritonkor.domain.services.handler.TechniqueHandler.PriceHandler;

public class HandlerFactory {

    //Client Handlers
    UsernameHandler usernameHandler;
    PasswordHandler passwordHandler;

    //Review Handlers
    OwnerIdHandler ownerIdHandler;
    TechnqiueIdHandler technqiueIdHandler;
    TextHandler textHandler;
    GradeHandler gradeHander;
    CreateTimeHandler createTimeHandler;

    //Technique Handlers
    PriceHandler priceHandler;
    CompanyHandler companyHandler;
    ModelHandler modelHandler;

    public HandlerFactory() {
        usernameHandler = new UsernameHandler();
        passwordHandler = new PasswordHandler();

        ownerIdHandler = new OwnerIdHandler();
        technqiueIdHandler = new TechnqiueIdHandler();
        textHandler = new TextHandler();
        gradeHander = new GradeHandler();
        createTimeHandler= new CreateTimeHandler();

        priceHandler = new PriceHandler();
        companyHandler = new CompanyHandler();
        modelHandler = new ModelHandler();

        usernameHandler.setNextHandler(passwordHandler);

        ownerIdHandler.setNextHandler(technqiueIdHandler);
        technqiueIdHandler.setNextHandler(textHandler);
        textHandler.setNextHandler(gradeHander);
        gradeHander.setNextHandler(createTimeHandler);

        priceHandler.setNextHandler(companyHandler);
        companyHandler.setNextHandler(modelHandler);
    }

    public UsernameHandler getUsernameHandler() {
        return usernameHandler;
    }

    public PasswordHandler getPasswordHandler() { return passwordHandler; }

    public OwnerIdHandler getOwnerIdHandler() {
        return ownerIdHandler;
    }

    public TechnqiueIdHandler getTechnqiueIdHandler() {
        return technqiueIdHandler;
    }

    public TextHandler getTextHandler() {
        return textHandler;
    }

    public GradeHandler getGradeHander() {
        return gradeHander;
    }

    public CreateTimeHandler getCreateTimeHandler() {
        return createTimeHandler;
    }

    public static HandlerFactory getInstance() {
        return HandlerFactory.InstanceHolder.INSTANCE;
    }

    public PriceHandler getPriceHandler() {
        return priceHandler;
    }

    public CompanyHandler getCompanyHandler() {
        return companyHandler;
    }

    public ModelHandler getModelHandler() {
        return modelHandler;
    }

    /**
     * Holder class for the Singleton pattern.
     */
    private static class InstanceHolder {

        /** The Singleton instance of HandlerFactory. */
        public static final HandlerFactory INSTANCE = new HandlerFactory();
    }
}
