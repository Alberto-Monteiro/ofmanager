import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ArtefatoComponentsPage, ArtefatoDeleteDialog, ArtefatoUpdatePage } from './artefato.page-object';

const expect = chai.expect;

describe('Artefato e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let artefatoComponentsPage: ArtefatoComponentsPage;
  let artefatoUpdatePage: ArtefatoUpdatePage;
  let artefatoDeleteDialog: ArtefatoDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Artefatoes', async () => {
    await navBarPage.goToEntity('artefato');
    artefatoComponentsPage = new ArtefatoComponentsPage();
    await browser.wait(ec.visibilityOf(artefatoComponentsPage.title), 5000);
    expect(await artefatoComponentsPage.getTitle()).to.eq('ofmanagerApp.artefato.home.title');
    await browser.wait(ec.or(ec.visibilityOf(artefatoComponentsPage.entities), ec.visibilityOf(artefatoComponentsPage.noResult)), 1000);
  });

  it('should load create Artefato page', async () => {
    await artefatoComponentsPage.clickOnCreateButton();
    artefatoUpdatePage = new ArtefatoUpdatePage();
    expect(await artefatoUpdatePage.getPageTitle()).to.eq('ofmanagerApp.artefato.home.createOrEditLabel');
    await artefatoUpdatePage.cancel();
  });

  it('should create and save Artefatoes', async () => {
    const nbButtonsBeforeCreate = await artefatoComponentsPage.countDeleteButtons();

    await artefatoComponentsPage.clickOnCreateButton();

    await promise.all([
      artefatoUpdatePage.setLocalDoArtefatoInput('localDoArtefato'),
      artefatoUpdatePage.setExtensaoInput('extensao'),
      artefatoUpdatePage.complexidadeSelectLastOption(),
      artefatoUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
    ]);

    expect(await artefatoUpdatePage.getLocalDoArtefatoInput()).to.eq(
      'localDoArtefato',
      'Expected LocalDoArtefato value to be equals to localDoArtefato'
    );
    expect(await artefatoUpdatePage.getExtensaoInput()).to.eq('extensao', 'Expected Extensao value to be equals to extensao');
    const selectedArtefatoDeTest = artefatoUpdatePage.getArtefatoDeTestInput();
    if (await selectedArtefatoDeTest.isSelected()) {
      await artefatoUpdatePage.getArtefatoDeTestInput().click();
      expect(await artefatoUpdatePage.getArtefatoDeTestInput().isSelected(), 'Expected artefatoDeTest not to be selected').to.be.false;
    } else {
      await artefatoUpdatePage.getArtefatoDeTestInput().click();
      expect(await artefatoUpdatePage.getArtefatoDeTestInput().isSelected(), 'Expected artefatoDeTest to be selected').to.be.true;
    }
    expect(await artefatoUpdatePage.getCreatedDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdDate value to be equals to 2000-12-31'
    );

    await artefatoUpdatePage.save();
    expect(await artefatoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await artefatoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Artefato', async () => {
    const nbButtonsBeforeDelete = await artefatoComponentsPage.countDeleteButtons();
    await artefatoComponentsPage.clickOnLastDeleteButton();

    artefatoDeleteDialog = new ArtefatoDeleteDialog();
    expect(await artefatoDeleteDialog.getDialogTitle()).to.eq('ofmanagerApp.artefato.delete.question');
    await artefatoDeleteDialog.clickOnConfirmButton();

    expect(await artefatoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
