import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

import HomeIcon from '@/common/component/icon/HomeIcon.vue'
import HomeOutlineIcon from '@/common/component/icon/HomeOutlineIcon.vue'
import RegisterIcon from '@/common/component/icon/RegisterIcon.vue'
import RegisterOutlineIcon from '@/common/component/icon/RegisterOutlineIcon.vue'
import MyPageIcon from '@/common/component/icon/MyPageIcon.vue'
import MyPageOutlineIcon from '@/common/component/icon/MyPageOutlineIcon.vue'
import ArrowLeftIcon from '@/common/component/icon/ArrowLeftIcon.vue'
import ArrowLeftWhiteIcon from '@/common/component/icon/ArrowLeftWhiteIcon.vue'
import DetailOutlineIcon from '@/common/component/icon/DetailOutlineIcon.vue'
import ShareOutlineIcon from '@/common/component/icon/ShareOutlineIcon.vue'
import CopyOutlineIcon from '@/common/component/icon/CopyOutlineIcon.vue'
import AddIcon from '@/common/component/icon/AddIcon.vue'
import CloseIcon from '@/common/component/icon/CloseIcon.vue'
import EditOutlineIcon from '@/common/component/icon/EditOutlineIcon.vue'
import UserInviteOutlineIcon from '@/common/component/icon/UserInviteOutlineIcon.vue'
import DeleteOutlineIcon from '@/common/component/icon/DeleteOutlineIcon.vue'
import LocationOutlineIcon from '@/common/component/icon/LocationOutlineIcon.vue'
import OutOutlineIcon from '@/common/component/icon/OutOutlineIcon.vue'
import FlagPlusIcon from '@/common/component/icon/FlagPlusIcon.vue'
import MenuIcon from '@/common/component/icon/MenuIcon.vue'
import CheckRoundIcon from '@/common/component/icon/CheckRoundIcon.vue'
import InfoOutlineIcon from '@/common/component/icon/InfoOutlineIcon.vue'
import MapOutlineIcon from '@/common/component/icon/MapOutlineIcon.vue'
import CameraOutlineIcon from '@/common/component/icon/CameraOutlineIcon.vue'

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    values: {
      home: {
        component: HomeIcon
      },
      home_outline: {
        component: HomeOutlineIcon
      },
      register: {
        component: RegisterIcon
      },
      register_outline: {
        component: RegisterOutlineIcon
      },
      mypage: {
        component: MyPageIcon
      },
      mypage_outline: {
        component: MyPageOutlineIcon
      },
      arrow_left: {
        component: ArrowLeftIcon
      },
      arrow_left_white: {
        component: ArrowLeftWhiteIcon
      },
      detail_outline: {
        component: DetailOutlineIcon
      },
      share_outline: {
        component: ShareOutlineIcon
      },
      copy_outline: {
        component: CopyOutlineIcon
      },
      add: {
        component: AddIcon
      },
      close: {
        component: CloseIcon
      },
      edit_outline: {
        component: EditOutlineIcon
      },
      user_invite_outline_icon: {
        component: UserInviteOutlineIcon
      },
      delete_outline: {
        component: DeleteOutlineIcon
      },
      location_outline: {
        component: LocationOutlineIcon
      },
      out_outline: {
        component: OutOutlineIcon
      },
      flag_plus: {
        component: FlagPlusIcon
      },
      menu: {
        component: MenuIcon
      },
      check_round: {
        component: CheckRoundIcon
      },
      info_outline: {
        component: InfoOutlineIcon
      },
      map_outline: {
        component: MapOutlineIcon
      },
      camera_outline: {
        component: CameraOutlineIcon
      }
    }
  }
});
