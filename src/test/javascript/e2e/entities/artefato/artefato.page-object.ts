import { element, by, ElementFinder } from 'protractor';

export class ArtefatoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('of-artefato div table .btn-danger'));
  title = element.all(by.css('of-artefato div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ArtefatoUpdatePage {
  pageTitle = element(by.id('of-artefato-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  localDoArtefatoInput = element(by.id('field_localDoArtefato'));
  extensaoInput = element(by.id('field_extensao'));
  complexidadeSelect = element(by.id('field_complexidade'));
  artefatoDeTestInput = element(by.id('field_artefatoDeTest'));
  createdDateInput = element(by.id('field_createdDate'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setLocalDoArtefatoInput(localDoArtefato: string): Promise<void> {
    await this.localDoArtefatoInput.sendKeys(localDoArtefato);
  }

  async getLocalDoArtefatoInput(): Promise<string> {
    return await this.localDoArtefatoInput.getAttribute('value');
  }

  async setExtensaoInput(extensao: string): Promise<void> {
    await this.extensaoInput.sendKeys(extensao);
  }

  async getExtensaoInput(): Promise<string> {
    return await this.extensaoInput.getAttribute('value');
  }

  async setComplexidadeSelect(complexidade: string): Promise<void> {
    await this.complexidadeSelect.sendKeys(complexidade);
  }

  async getComplexidadeSelect(): Promise<string> {
    return await this.complexidadeSelect.element(by.css('option:checked')).getText();
  }

  async complexidadeSelectLastOption(): Promise<void> {
    await this.complexidadeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  getArtefatoDeTestInput(): ElementFinder {
    return this.artefatoDeTestInput;
  }

  async setCreatedDateInput(createdDate: string): Promise<void> {
    await this.createdDateInput.sendKeys(createdDate);
  }

  async getCreatedDateInput(): Promise<string> {
    return await this.createdDateInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ArtefatoDeleteDialog {
  private dialogTitle = element(by.id('of-delete-artefato-heading'));
  private confirmButton = element(by.id('of-confirm-delete-artefato'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
