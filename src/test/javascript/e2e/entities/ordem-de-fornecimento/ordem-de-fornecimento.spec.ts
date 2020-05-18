import { browser, ExpectedConditions as ec, promise, protractor } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  OrdemDeFornecimentoComponentsPage,
  OrdemDeFornecimentoDeleteDialog,
  OrdemDeFornecimentoUpdatePage
} from './ordem-de-fornecimento.page-object';

const expect = chai.expect;

describe('OrdemDeFornecimento e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ordemDeFornecimentoComponentsPage: OrdemDeFornecimentoComponentsPage;
  let ordemDeFornecimentoUpdatePage: OrdemDeFornecimentoUpdatePage;
  let ordemDeFornecimentoDeleteDialog: OrdemDeFornecimentoDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load OrdemDeFornecimentos', async () => {
    await navBarPage.goToEntity('ordem-de-fornecimento');
    ordemDeFornecimentoComponentsPage = new OrdemDeFornecimentoComponentsPage();
    await browser.wait(ec.visibilityOf(ordemDeFornecimentoComponentsPage.title), 5000);
    expect(await ordemDeFornecimentoComponentsPage.getTitle()).to.eq('ofmanagerApp.ordemDeFornecimento.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(ordemDeFornecimentoComponentsPage.entities), ec.visibilityOf(ordemDeFornecimentoComponentsPage.noResult)),
      1000
    );
  });

  it('should load create OrdemDeFornecimento page', async () => {
    await ordemDeFornecimentoComponentsPage.clickOnCreateButton();
    ordemDeFornecimentoUpdatePage = new OrdemDeFornecimentoUpdatePage();
    expect(await ordemDeFornecimentoUpdatePage.getPageTitle()).to.eq('ofmanagerApp.ordemDeFornecimento.home.createOrEditLabel');
    await ordemDeFornecimentoUpdatePage.cancel();
  });

  it('should create and save OrdemDeFornecimentos', async () => {
    const nbButtonsBeforeCreate = await ordemDeFornecimentoComponentsPage.countDeleteButtons();

    await ordemDeFornecimentoComponentsPage.clickOnCreateButton();

    await promise.all([
      ordemDeFornecimentoUpdatePage.setNumeroInput('5'),
      ordemDeFornecimentoUpdatePage.estadoSelectLastOption(),
      ordemDeFornecimentoUpdatePage.setObservacaoDoGestorInput('observacaoDoGestor'),
      ordemDeFornecimentoUpdatePage.setCreatedByInput('createdBy'),
      ordemDeFornecimentoUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      ordemDeFornecimentoUpdatePage.setLastModifiedByInput('lastModifiedBy'),
      ordemDeFornecimentoUpdatePage.setLastModifiedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      ordemDeFornecimentoUpdatePage.setValorUstibbInput('5'),
      ordemDeFornecimentoUpdatePage.setDataDeEntregaInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      ordemDeFornecimentoUpdatePage.gestorDaOfSelectLastOption(),
      ordemDeFornecimentoUpdatePage.donoDaOfSelectLastOption()
    ]);

    expect(await ordemDeFornecimentoUpdatePage.getNumeroInput()).to.eq('5', 'Expected numero value to be equals to 5');
    expect(await ordemDeFornecimentoUpdatePage.getObservacaoDoGestorInput()).to.eq(
      'observacaoDoGestor',
      'Expected ObservacaoDoGestor value to be equals to observacaoDoGestor'
    );
    expect(await ordemDeFornecimentoUpdatePage.getCreatedByInput()).to.eq(
      'createdBy',
      'Expected CreatedBy value to be equals to createdBy'
    );
    expect(await ordemDeFornecimentoUpdatePage.getCreatedDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdDate value to be equals to 2000-12-31'
    );
    expect(await ordemDeFornecimentoUpdatePage.getLastModifiedByInput()).to.eq(
      'lastModifiedBy',
      'Expected LastModifiedBy value to be equals to lastModifiedBy'
    );
    expect(await ordemDeFornecimentoUpdatePage.getLastModifiedDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected lastModifiedDate value to be equals to 2000-12-31'
    );
    expect(await ordemDeFornecimentoUpdatePage.getValorUstibbInput()).to.eq('5', 'Expected valorUstibb value to be equals to 5');
    expect(await ordemDeFornecimentoUpdatePage.getDataDeEntregaInput()).to.contain(
      '2001-01-01T02:30',
      'Expected dataDeEntrega value to be equals to 2000-12-31'
    );

    await ordemDeFornecimentoUpdatePage.save();
    expect(await ordemDeFornecimentoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ordemDeFornecimentoComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last OrdemDeFornecimento', async () => {
    const nbButtonsBeforeDelete = await ordemDeFornecimentoComponentsPage.countDeleteButtons();
    await ordemDeFornecimentoComponentsPage.clickOnLastDeleteButton();

    ordemDeFornecimentoDeleteDialog = new OrdemDeFornecimentoDeleteDialog();
    expect(await ordemDeFornecimentoDeleteDialog.getDialogTitle()).to.eq('ofmanagerApp.ordemDeFornecimento.delete.question');
    await ordemDeFornecimentoDeleteDialog.clickOnConfirmButton();

    expect(await ordemDeFornecimentoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
