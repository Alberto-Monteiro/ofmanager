import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ServicoOfComponentsPage, ServicoOfDeleteDialog, ServicoOfUpdatePage } from './servico-of.page-object';

const expect = chai.expect;

describe('ServicoOf e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let servicoOfComponentsPage: ServicoOfComponentsPage;
  let servicoOfUpdatePage: ServicoOfUpdatePage;
  let servicoOfDeleteDialog: ServicoOfDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ServicoOfs', async () => {
    await navBarPage.goToEntity('servico-of');
    servicoOfComponentsPage = new ServicoOfComponentsPage();
    await browser.wait(ec.visibilityOf(servicoOfComponentsPage.title), 5000);
    expect(await servicoOfComponentsPage.getTitle()).to.eq('ofmanagerApp.servicoOf.home.title');
    await browser.wait(ec.or(ec.visibilityOf(servicoOfComponentsPage.entities), ec.visibilityOf(servicoOfComponentsPage.noResult)), 1000);
  });

  it('should load create ServicoOf page', async () => {
    await servicoOfComponentsPage.clickOnCreateButton();
    servicoOfUpdatePage = new ServicoOfUpdatePage();
    expect(await servicoOfUpdatePage.getPageTitle()).to.eq('ofmanagerApp.servicoOf.home.createOrEditLabel');
    await servicoOfUpdatePage.cancel();
  });

  it('should create and save ServicoOfs', async () => {
    const nbButtonsBeforeCreate = await servicoOfComponentsPage.countDeleteButtons();

    await servicoOfComponentsPage.clickOnCreateButton();

    await promise.all([
      servicoOfUpdatePage.setUseridInput('5'),
      servicoOfUpdatePage.setNumeroInput('5'),
      servicoOfUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      servicoOfUpdatePage.gestorDaOfSelectLastOption(),
      servicoOfUpdatePage.donoDaOfSelectLastOption()
    ]);

    expect(await servicoOfUpdatePage.getUseridInput()).to.eq('5', 'Expected userid value to be equals to 5');
    expect(await servicoOfUpdatePage.getNumeroInput()).to.eq('5', 'Expected numero value to be equals to 5');
    expect(await servicoOfUpdatePage.getCreatedDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdDate value to be equals to 2000-12-31'
    );

    await servicoOfUpdatePage.save();
    expect(await servicoOfUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await servicoOfComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ServicoOf', async () => {
    const nbButtonsBeforeDelete = await servicoOfComponentsPage.countDeleteButtons();
    await servicoOfComponentsPage.clickOnLastDeleteButton();

    servicoOfDeleteDialog = new ServicoOfDeleteDialog();
    expect(await servicoOfDeleteDialog.getDialogTitle()).to.eq('ofmanagerApp.servicoOf.delete.question');
    await servicoOfDeleteDialog.clickOnConfirmButton();

    expect(await servicoOfComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
